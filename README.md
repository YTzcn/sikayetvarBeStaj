2025-2026 Yaz Dönemi Şikayetvar Backend&Ai Staj

## OpenAI Sohbet Entegrasyonu

DTO, Service ve Global Exception Handling yapılarıyla istenen istek/cevap akışı oluşturuldu.
Unit testleri yazıldı. Servis yazılırken class bağımlılıklarının azaltılması için interface
kullanıldı, bu sayede ileride eklenecek yeni AI providerlar için hazırlık yapıldı.

Kod `com.sikayetvar.beStaj.ai` paketinde:
- `dto` – istek/cevap modelleri
- `service` – `AiChatService` arayüzü ve OpenAI implementasyonu
- `controller` – `/api/ai/chat` endpoint'i
- `exception` – global hata yönetimi
- `config` – OpenAI bağlantı ayarları

### Çalıştırmadan önce

`application.properties.example` dosyasını `application.properties` olarak kopyala, OpenAI API
anahtarını `openai.api-key` alanına yaz:

```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

### Çalıştırma

```bash
./gradlew bootRun
```

```bash
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Merhaba, nasılsın?"}'
```

### Testler

```bash
./gradlew test
```
