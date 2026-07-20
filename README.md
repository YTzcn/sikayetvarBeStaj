2025-2026 Yaz Dönemi Şikayetvar Backend&Ai Staj

## OpenAI Sohbet Entegrasyonu

`com.sikayetvar.beStaj.ai` paketi, OpenAI Chat Completions API'si ile temel bir istek/cevap
akışı kurar.

### Kurulum

1. `src/main/resources/application.properties.example` dosyasını aynı klasörde
   `application.properties` olarak kopyalayın (bu dosya `.gitignore` içinde olduğu için
   sürüm kontrolüne dahil edilmez):
   ```bash
   cp src/main/resources/application.properties.example src/main/resources/application.properties
   ```
2. `OPENAI_API_KEY` adında bir ortam değişkeni tanımlayın (API anahtarını değere girin):
   ```bash
   export OPENAI_API_KEY=sk-...
   ```
3. Uygulamayı çalıştırın:
   ```bash
   ./gradlew bootRun
   ```

### Kullanım

```bash
curl -X POST http://localhost:8080/api/ai/chat \
  -H "Content-Type: application/json" \
  -d '{"message": "Merhaba, nasılsın?"}'
```

Başarılı bir yanıt şu şekilde döner:

```json
{
  "reply": "Merhaba! İyiyim, teşekkür ederim..."
}
```

`openai.model` ve `openai.timeout-seconds` gibi ayarlar `application.properties` dosyasından
değiştirilebilir.
