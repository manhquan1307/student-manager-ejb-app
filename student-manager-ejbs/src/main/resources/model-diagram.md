# Sơ đồ mô hình hóa Student-Course

## Mô tả
Sơ đồ PlantUML mô tả mối quan hệ nhiều-nhiều giữa Student và Course trong hệ thống quản lý sinh viên sử dụng EJB CMP.

## Cách xem sơ đồ

### Cách 1: Sử dụng PlantUML Online
1. Truy cập: http://www.plantuml.com/plantuml/uml/
2. Copy nội dung file `model.puml`
3. Paste vào editor và xem kết quả

### Cách 2: Sử dụng VS Code Extension
1. Cài đặt extension "PlantUML" trong VS Code
2. Mở file `model.puml`
3. Nhấn `Alt+D` để xem preview

### Cách 3: Sử dụng Maven Plugin
Thêm vào `pom.xml`:
```xml
<plugin>
    <groupId>net.sourceforge.plantuml</groupId>
    <artifactId>plantuml-maven-plugin</artifactId>
    <version>1.4.1</version>
    <configuration>
        <sourceFiles>
            <directory>src/main/resources</directory>
            <includes>
                <include>**/*.puml</include>
            </includes>
        </sourceFiles>
    </configuration>
</plugin>
```

## Cấu trúc mô hình

### Student Entity Bean
- **Primary Key**: `studentId` (Long)
- **CMP Fields**: 
  - `studentId`: ID sinh viên
  - `name`: Tên sinh viên
  - `email`: Email sinh viên
- **CMR Field**: 
  - `courses`: Collection<Course> - Danh sách khóa học sinh viên đăng ký
- **Business Methods**:
  - `addCourse(Course)`: Thêm khóa học cho sinh viên
  - `getCourseList()`: Liệt kê danh sách khóa học của sinh viên

### Course Entity Bean
- **Primary Key**: `courseId` (Long)
- **CMP Fields**:
  - `courseId`: ID khóa học
  - `courseName`: Tên khóa học
  - `courseCode`: Mã khóa học
- **CMR Field**:
  - `students`: Collection<Student> - Danh sách sinh viên đăng ký khóa học

### Quan hệ Many-to-Many
- Một sinh viên có thể đăng ký nhiều khóa học
- Một khóa học có thể có nhiều sinh viên
- Quan hệ được quản lý bởi Container (CMR)

