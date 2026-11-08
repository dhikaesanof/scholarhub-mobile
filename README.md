# ScholarHub

Aplikasi Android berbasis Kotlin dan Jetpack Compose untuk melihat informasi beasiswa. Katalog lokal ada di [scholarships.json](app/src/main/assets/scholarships.json). Saat ini aplikasi membuka layar detail. Daftar beasiswa dan alur ScholarMatch masih direncanakan.

## Menjalankan project

Buka folder root ScholarHub di Android Studio dan sinkronkan Gradle. Gunakan konfigurasi SDK dan JDK yang ditetapkan project, lalu jalankan modul `app` pada emulator atau perangkat. Perintah build dan pengecekan tersedia di [AGENTS.md](AGENTS.md#mengerjakan-satu-tugas).

## Cara kerja dengan AI

1. Buka folder root **ScholarHub** sebagai project di **Codex** atau workspace di **Google Antigravity**.
2. Mulai tugas dengan prompt seperti “Baca `AGENTS.md` di root repo sebelum mengedit. Kerjakan [tugas], ikuti [frame Figma jika ada], lalu laporkan apa yang berhasil dan belum diuji.”
3. Review diff dan hasil pengecekan sebelum menggabungkan perubahan.

Antigravity mendeteksi `AGENTS.md` di root sebagai aturan workspace aktif. Jika aturan belum terlihat di sesi yang dipakai, minta agent membuka file itu secara eksplisit. Seluruh aturan project ada di [AGENTS.md](AGENTS.md). [Dokumentasi resmi aturan Antigravity](https://antigravity.google/docs/rules/)
