const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  username: {
    type: String,
    required: [true, '사용자명은 필수입니다.'],
    unique: true,
    trim: true,
    minlength: [3, '사용자명은 최소 3자 이상이어야 합니다.'],
    maxlength: [30, '사용자명은 최대 30자까지 가능합니다.']
  },
  email: {
    type: String,
    required: [true, '이메일은 필수입니다.'],
    unique: true,
    lowercase: true,
    trim: true,
    match: [/^\w+([.-]?\w+)*@\w+([.-]?\w+)*(\.\w{2,3})+$/, '유효한 이메일 주소를 입력해주세요.']
  },
  password: {
    type: String,
    required: [true, '비밀번호는 필수입니다.'],
    minlength: [6, '비밀번호는 최소 6자 이상이어야 합니다.']
  },
  firstName: {
    type: String,
    trim: true,
    maxlength: [50, '이름은 최대 50자까지 가능합니다.']
  },
  lastName: {
    type: String,
    trim: true,
    maxlength: [50, '성은 최대 50자까지 가능합니다.']
  },
  isActive: {
    type: Boolean,
    default: true
  },
  role: {
    type: String,
    enum: ['user', 'admin'],
    default: 'user'
  }
}, {
  timestamps: true // createdAt, updatedAt 자동 생성
});

// 가상 필드: 전체 이름
userSchema.virtual('fullName').get(function() {
  return `${this.firstName || ''} ${this.lastName || ''}`.trim();
});

// JSON 변환 시 가상 필드 포함
userSchema.set('toJSON', { virtuals: true });

module.exports = mongoose.model('User', userSchema);
