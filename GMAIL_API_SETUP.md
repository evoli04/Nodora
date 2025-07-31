# 📧 Gmail API Setup Rehberi - Nodora Backend

Bu rehber, Nodora backend uygulamasında "Şifremi Unuttum" özelliği için Gmail API'sini nasıl aktifleştireceğinizi açıklar.

## 🚀 Adım 1: Google Cloud Console'da Proje Oluşturma

1. [Google Cloud Console](https://console.cloud.google.com/)'a gidin
2. Yeni bir proje oluşturun veya mevcut projeyi seçin
3. Proje adı: `nodora-email-service` (veya istediğiniz bir isim)

## 🔧 Adım 2: Gmail API'sini Aktifleştirme

1. Sol menüden **APIs & Services > Library** seçin
2. "Gmail API" arayın ve seçin
3. **Enable** butonuna tıklayın

## 🔐 Adım 3: OAuth 2.0 Credentials Oluşturma

1. **APIs & Services > Credentials** sayfasına gidin
2. **+ CREATE CREDENTIALS** > **OAuth client ID** seçin
3. Application type: **Desktop application** seçin
4. Name: `Nodora Gmail Service`
5. **Authorized redirect URIs** kısmına ekleyin:
   - `http://localhost:8080/oauth2/callback`
   - `http://localhost:8888/Callback` (OAuth flow için)

## 📁 Adım 4: Credentials Dosyasını İndirme

1. Oluşturulan credential'a tıklayın
2. **Download JSON** butonuna tıklayın
3. İndirilen dosyayı `demo/src/main/resources/gmail-credentials.json` olarak kaydedin

**ÖNEMLI:** Bu dosyayı Git'e commit etmeyin! `.gitignore`'a ekleyin.

## ⚙️ Adım 5: Environment Variables Ayarlama

`application.properties` dosyasında şu değişkenleri ayarlayın:

```properties
# Google OAuth Konfigürasyonu
google.oauth.client.id=YOUR_CLIENT_ID_FROM_JSON_FILE
google.oauth.client.secret=YOUR_CLIENT_SECRET_FROM_JSON_FILE
google.oauth.redirect.uri=http://localhost:8080/oauth2/callback

# Gmail API Konfigürasyonu
gmail.api.enabled=true
gmail.api.credentials.path=src/main/resources/gmail-credentials.json
gmail.api.tokens.path=src/main/resources/tokens
gmail.api.application.name=Nodora Email Service
```

## 🧪 Adım 6: İlk Test

1. Backend uygulamasını başlatın
2. Şu endpoint'i test edin: `POST /api/auth/test-email`
3. İlk çalıştırmada tarayıcı açılacak ve Google'a giriş yapmanız istenecek
4. İzinleri onaylayın
5. `tokens` klasörü otomatik oluşturulacak (bu da `.gitignore`'a eklenmelidir)

## 📧 Adım 7: Forgot Password Test

```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com"
  }'
```

## 🔒 Güvenlik Notları

1. **gmail-credentials.json** dosyasını asla Git'e commit etmeyin
2. **tokens** klasörünü de Git'e commit etmeyin
3. Production'da environment variables kullanın
4. OAuth consent screen'i production'da verified olmalı

## 📝 .gitignore'a Eklenecekler

```
# Gmail API
src/main/resources/gmail-credentials.json
src/main/resources/tokens/
tokens/

# Environment files
.env
```

## 🐛 Troubleshooting

### Problem: "The OAuth client was not found"
**Çözüm:** Client ID ve Client Secret'ı kontrol edin

### Problem: "Redirect URI mismatch"  
**Çözüm:** Google Console'da redirect URI'ları kontrol edin

### Problem: "Gmail API has not been used in project"
**Çözüm:** Gmail API'sini enable etmeyi unutmuş olabilirsiniz

### Problem: "Access blocked: This app's request is invalid"
**Çözüm:** OAuth consent screen'i configure etmelisiniz

## ✅ Gmail API Çalışma Şeması

1. **Şifremi Unuttum** butonu tıklandığında
2. Backend Gmail OAuth API'yi dener (Öncelik)
3. Başarısız olursa Outlook SMTP'ye düşer (Fallback)
4. Her iki yöntem de başarısız olursa hata mesajı

## 🎯 Son Test

Test email endpoint'i ile sistemi test edin:
```
POST /api/auth/test-email
```

Parametreler:
- `toEmail`: Gönderilecek adres
- `fromEmail`: Gönderen adres (OAuth yapan kullanıcı)
- `subject`: E-posta konusu (opsiyonel)
- `message`: E-posta mesajı (opsiyonel)

---

**Not:** İlk kurulumda token authorization gerektirdiği için development ortamında çalıştırmanız gerekir.