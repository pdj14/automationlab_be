const express = require('express');
const router = express.Router();
const User = require('../models/User');

// 모든 사용자 조회 (관리자용)
router.get('/', async (req, res) => {
  try {
    const users = await User.find({}, '-password').sort({ createdAt: -1 });
    res.json({
      success: true,
      data: users,
      count: users.length
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '사용자 목록을 가져오는 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 특정 사용자 조회
router.get('/:id', async (req, res) => {
  try {
    const user = await User.findById(req.params.id, '-password');
    if (!user) {
      return res.status(404).json({
        success: false,
        error: '사용자를 찾을 수 없습니다.'
      });
    }
    res.json({
      success: true,
      data: user
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '사용자 정보를 가져오는 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 새 사용자 생성
router.post('/', async (req, res) => {
  try {
    const { username, email, password, firstName, lastName } = req.body;
    
    // 필수 필드 검증
    if (!username || !email || !password) {
      return res.status(400).json({
        success: false,
        error: '사용자명, 이메일, 비밀번호는 필수입니다.'
      });
    }

    // 중복 사용자 확인
    const existingUser = await User.findOne({
      $or: [{ username }, { email }]
    });

    if (existingUser) {
      return res.status(400).json({
        success: false,
        error: '이미 존재하는 사용자명 또는 이메일입니다.'
      });
    }

    // 새 사용자 생성
    const user = new User({
      username,
      email,
      password, // 실제로는 해시화해야 함
      firstName,
      lastName
    });

    const savedUser = await user.save();
    
    // 비밀번호 제외하고 응답
    const userResponse = savedUser.toObject();
    delete userResponse.password;

    res.status(201).json({
      success: true,
      data: userResponse,
      message: '사용자가 성공적으로 생성되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '사용자 생성 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 사용자 정보 수정
router.put('/:id', async (req, res) => {
  try {
    const { firstName, lastName, isActive } = req.body;
    
    const updatedUser = await User.findByIdAndUpdate(
      req.params.id,
      { firstName, lastName, isActive },
      { new: true, runValidators: true }
    ).select('-password');

    if (!updatedUser) {
      return res.status(404).json({
        success: false,
        error: '사용자를 찾을 수 없습니다.'
      });
    }

    res.json({
      success: true,
      data: updatedUser,
      message: '사용자 정보가 성공적으로 수정되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '사용자 정보 수정 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

// 사용자 삭제
router.delete('/:id', async (req, res) => {
  try {
    const deletedUser = await User.findByIdAndDelete(req.params.id);
    
    if (!deletedUser) {
      return res.status(404).json({
        success: false,
        error: '사용자를 찾을 수 없습니다.'
      });
    }

    res.json({
      success: true,
      message: '사용자가 성공적으로 삭제되었습니다.'
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      error: '사용자 삭제 중 오류가 발생했습니다.',
      message: error.message
    });
  }
});

module.exports = router;
