// MongoDB 초기화 스크립트
db = db.getSiblingDB('aubotmationlab');

// 샘플 3D 객체 데이터 생성
db.objects.insertMany([
  {
    name: "Industrial Robot",
    category: "ROBOT",
    description: "6-axis industrial robot for manufacturing",
    glbFile: "/models/industrial-robot.glb",
    thumbnailFile: "/thumbnails/industrial-robot.jpg",
    width: 2.5,
    depth: 2.0,
    height: 3.2,
    rotation: 0.0,
    color: "#FF6B35",
    instancingEnabled: true
  },
  {
    name: "Conveyor Belt",
    category: "EQUIPMENT",
    description: "Automated conveyor belt system",
    glbFile: "/models/conveyor-belt.glb",
    thumbnailFile: "/thumbnails/conveyor-belt.jpg",
    width: 5.0,
    depth: 1.5,
    height: 1.0,
    rotation: 90.0,
    color: "#4A90E2",
    instancingEnabled: false
  },
  {
    name: "Smart TV",
    category: "AV",
    description: "55-inch 4K Smart TV",
    glbFile: "/models/smart-tv.glb",
    thumbnailFile: "/thumbnails/smart-tv.jpg",
    width: 1.2,
    depth: 0.1,
    height: 0.7,
    rotation: 0.0,
    color: "#000000",
    instancingEnabled: true
  },
  {
    name: "Refrigerator",
    category: "APPLIANCES",
    description: "Large capacity refrigerator",
    glbFile: "/models/refrigerator.glb",
    thumbnailFile: "/thumbnails/refrigerator.jpg",
    width: 0.8,
    depth: 0.7,
    height: 1.8,
    rotation: 0.0,
    color: "#FFFFFF",
    instancingEnabled: false
  }
]);

// 인덱스 생성
db.objects.createIndex({ "name": 1 }, { unique: true });
db.objects.createIndex({ "category": 1 });
db.objects.createIndex({ "instancingEnabled": 1 });

print("MongoDB 초기화 완료: " + db.objects.countDocuments() + "개의 샘플 객체가 생성되었습니다.");
