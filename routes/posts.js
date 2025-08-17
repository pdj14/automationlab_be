const express = require('express');
const router = express.Router();
const Post = require('../models/Post');
const User = require('../models/User');

// 모든 게시물 조회 (페이지네이션 포함)
router.get('/', async (req, res) => {
  try {
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 10;
    const category = req.query.category;
    const search = req.query.search;
    
    let query = { isPublished: true };
    
    // 카테고리 필터링
    if (category && category !== 'all') {
      query.category = category;
    }
    
    // 검색 기능
    if (search) {
      query.$text = { $search: search };
    }
    
    const skip = (page - 1) * limit;
    
    const posts = await Post.find(query)
      .populate('author', 'username firstName lastName')
      .sort({ publishedAt: -1 })
      .skip(skip)
      .limit(limit);
    
    const total = await Post.countDocuments(query);
    
    res.json({
      success: true,
      data: posts,
      pagination: {
        currentPage: page,
        totalPages: Math.ceil(total / limit),
        totalPosts: total,
        hasNext: page * limit < total,
        hasPrev: page > 1
      }
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '게시물 목록을 가져오는 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 특정 게시물 조회 (조회수 증가)
router.get('/:id', async (req, res) => {
  try {
    const post = await Post.findById(req.params.id)
      .populate('author', 'username firstName lastName')
      .populate('likes', 'username');
    
    if (!post) {
      return res.status(404).json({
        success: false,
        error: '게시물을 찾을 수 없습니다.'
      });
    }
    
    // 조회수 증가
    post.views += 1;
    await post.save();
    
    res.json({
      success: true,
      data: post
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '게시물을 가져오는 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 새 게시물 생성
router.post('/', async (req, res) => {
  try {
    const { title, content, tags, category, isPublished } = req.body;
    
    // 필수 필드 검증
    if (!title || !content) {
      return res.status(400).json({
        success: false,
        error: '제목과 내용은 필수입니다.'
      });
    }
    
    // 임시로 첫 번째 사용자를 작성자로 설정 (실제로는 인증된 사용자 ID 사용)
    const firstUser = await User.findOne();
    if (!firstUser) {
      return res.status(400).json({
        success: false,
        error: '사용자가 존재하지 않습니다.'
      });
    }
    
    const post = new Post({
      title,
      content,
      author: firstUser._id,
      tags: tags || [],
      category: category || 'general',
      isPublished: isPublished || false
    });
    
    const savedPost = await post.save();
    const populatedPost = await Post.findById(savedPost._id)
      .populate('author', 'username firstName lastName');
    
    res.status(201).json({
      success: true,
      data: populatedPost,
      message: '게시물이 성공적으로 생성되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '게시물 생성 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 게시물 수정
router.put('/:id', async (req, res) => {
  try {
    const { title, content, tags, category, isPublished } = req.body;
    
    const updatedPost = await Post.findByIdAndUpdate(
      req.params.id,
      { title, content, tags, category, isPublished },
      { new: true, runValidators: true }
    ).populate('author', 'username firstName lastName');
    
    if (!updatedPost) {
      return res.status(404).json({
        success: false,
        error: '게시물을 찾을 수 없습니다.'
      });
    }
    
    res.json({
      success: true,
      data: updatedPost,
      message: '게시물이 성공적으로 수정되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '게시물 수정 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 게시물 삭제
router.delete('/:id', async (req, res) => {
  try {
    const deletedPost = await Post.findByIdAndDelete(req.params.id);
    
    if (!deletedPost) {
      return res.status(404).json({
        success: false,
        error: '게시물을 찾을 수 없습니다.'
      });
    }
    
    res.json({
      success: true,
      message: '게시물이 성공적으로 삭제되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '게시물 삭제 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 게시물 좋아요/좋아요 취소
router.post('/:id/like', async (req, res) => {
  try {
    const post = await Post.findById(req.params.id);
    if (!post) {
      return res.status(404).json({
        success: false,
        error: '게시물을 찾을 수 없습니다.'
      });
    }
    
    // 임시로 첫 번째 사용자 ID 사용 (실제로는 인증된 사용자 ID 사용)
    const firstUser = await User.findOne();
    if (!firstUser) {
      return res.status(400).json({
        success: false,
        error: '사용자가 존재하지 않습니다.'
      });
    }
    
    const userId = firstUser._id;
    const isLiked = post.likes.includes(userId);
    
    if (isLiked) {
      // 좋아요 취소
      post.likes = post.likes.filter(id => !id.equals(userId));
    } else {
      // 좋아요 추가
      post.likes.push(userId);
    }
    
    await post.save();
    
    res.json({
      success: true,
      data: {
        isLiked: !isLiked,
        likesCount: post.likes.length
      },
      message: isLiked ? '좋아요가 취소되었습니다.' : '좋아요가 추가되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '좋아요 처리 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

module.exports = router;
