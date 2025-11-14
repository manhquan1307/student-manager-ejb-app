# Hướng dẫn Deploy dự án EJB

## 📦 File đã build
- **EAR file**: `student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear`

## 🗄️ Database
**Dự án này KHÔNG sử dụng database** - đây là demo về EJB dependency injection.

## 🚀 Cách Deploy

### Option 1: Deploy lên GlassFish

#### 1. Download và cài đặt GlassFish
- Download: https://glassfish.org/download/
- Giải nén và chạy:
```bash
# Windows
glassfish\bin\asadmin.bat start-domain

# Linux/Mac
glassfish/bin/asadmin start-domain
```

#### 2. Deploy EAR file
```bash
# Windows
glassfish\bin\asadmin.bat deploy student-manager-ear\target\student-manager-ear-0.0.1-SNAPSHOT.ear

# Linux/Mac
glassfish/bin/asadmin deploy student-manager-ear/target/student-manager-ear-0.0.1-SNAPSHOT.ear
```

#### 3. Hoặc deploy qua Admin Console
- Mở: http://localhost:4848
- Login (nếu có)
- Vào **Applications** → **Deploy**
- Chọn file `student-manager-ear-0.0.1-SNAPSHOT.ear`
- Click **OK**

#### 4. Truy cập ứng dụng
- Servlet: http://localhost:8080/StudentManager/hello
- Trang chủ: http://localhost:8080/StudentManager/

---

### Option 2: Deploy lên WildFly/JBoss

#### 1. Download WildFly
- Download: https://www.wildfly.org/downloads/
- Giải nén

#### 2. Start WildFly
```bash
# Windows
wildfly\bin\standalone.bat

# Linux/Mac
wildfly/bin/standalone.sh
```

#### 3. Deploy EAR file
**Cách 1: Copy vào deployment folder**
```bash
# Copy file EAR vào thư mục deployments
copy student-manager-ear\target\student-manager-ear-0.0.1-SNAPSHOT.ear wildfly\standalone\deployments\
```

**Cách 2: Deploy qua CLI**
```bash
wildfly\bin\jboss-cli.bat --connect
deploy student-manager-ear\target\student-manager-ear-0.0.1-SNAPSHOT.ear
```

**Cách 3: Deploy qua Admin Console**
- Mở: http://localhost:9990
- Vào **Deployments** → **Add**
- Upload file EAR

#### 4. Truy cập ứng dụng
- Servlet: http://localhost:8080/StudentManager/hello
- Trang chủ: http://localhost:8080/StudentManager/

---

### Option 3: Deploy lên Payara Server (Fork của GlassFish)

Tương tự như GlassFish, Payara Server tương thích với GlassFish.

---

## 🔍 Kiểm tra deployment

### Xem log
- **GlassFish**: `glassfish\domains\domain1\logs\server.log`
- **WildFly**: `wildfly\standalone\log\server.log`

### Xem deployed applications
- **GlassFish**: http://localhost:4848 → Applications
- **WildFly**: http://localhost:9990 → Deployments

---

## 🛠️ Troubleshooting

### Lỗi: Port đã được sử dụng
- GlassFish mặc định: 8080 (HTTP), 4848 (Admin)
- WildFly mặc định: 8080 (HTTP), 9990 (Admin)
- Đổi port trong file config hoặc dừng service đang dùng port đó

### Lỗi: ClassNotFoundException
- Kiểm tra Java version (cần Java 8+)
- Kiểm tra dependencies trong EAR file

### Undeploy
```bash
# GlassFish
asadmin undeploy student-manager-ear-0.0.1-SNAPSHOT

# WildFly CLI
undeploy student-manager-ear-0.0.1-SNAPSHOT.ear
```

---

## 📝 Lưu ý

1. **Java Version**: Cần Java 8 trở lên
2. **Không cần Database**: Dự án này chỉ demo EJB, không cần setup database
3. **Application Server**: Cần một trong các server: GlassFish, WildFly, Payara, hoặc JBoss

