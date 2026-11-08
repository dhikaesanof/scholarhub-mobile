# Panduan kerja ScholarHub

File ini adalah acuan utama untuk anggota tim dan agent yang mengerjakan repo ScholarHub. Gunakan project Android yang sudah ada dengan satu modul `:app`, Kotlin, Jetpack Compose Material 3, dan namespace `com.example.scholarhub`. Fase ini mencakup katalog, detail, form ScholarMatch, dan hasil ScholarMatch. Profil tersimpan, bookmark, dan pencarian katalog masuk fase berikutnya.

## Peta folder

Path Kotlin pada tabel relatif terhadap `app/src/main/java/com/example/scholarhub/`. Buat folder rencana saat fitur terkait mulai dikerjakan.

| Lokasi | Status dan tanggung jawab |
| --- | --- |
| `MainActivity.kt` | Ada. Entry point, memasang `ScholarHubTheme`, membuat repository lokal dan ViewModel. Saat ini langsung membuka detail dari ekstra `scholarshipId` dengan `SH28` sebagai default sementara. |
| `ui/theme/` dan `app/src/main/res/` | Ada. Tema Compose, warna, tipografi, font, dan aset Android. |
| `feature/scholarship/detail/` | Ada. `ScholarshipDetailScreen.kt` merender state dan meneruskan aksi. `ScholarshipDetailViewModel.kt` mengambil detail serta menangani state loading, siap, tidak ditemukan, dan error. |
| `feature/scholarship/detail/components/` | Rencana. Komponen khusus detail jika perlu dipisah. Untuk komponen yang benar-benar dipakai beberapa layar beasiswa, gunakan `feature/scholarship/components/`. |
| `feature/scholarship/list/` | Rencana. Tempat `ScholarshipListScreen.kt` dan `ScholarshipListViewModel.kt`. |
| `domain/scholarship/` | Ada. `Scholarship.kt` berisi model katalog, detail, dan aturan. `ScholarshipRepository.kt` adalah kontrak untuk katalog, detail menurut ID, dan opsi prioritas. |
| `data/scholarship/` | Ada. `LocalScholarshipRepository.kt` membaca asset melalui `AssetManager`. `ScholarshipJsonParser.kt` memvalidasi versi skema dan memetakan JSON ke model. |
| `app/src/main/assets/scholarships.json` | Ada. Satu sumber katalog untuk daftar, detail, dan ScholarMatch. |
| `navigation/AppNavGraph.kt` | Rencana. Hubungkan daftar dan hasil ScholarMatch ke detail lewat `scholarshipId`. Form dan hasil berbagi ViewModel selama alurnya aktif. |
| `feature/scholarmatch/` | Rencana. Tempat `ScholarMatchFormScreen.kt`, `ScholarMatchResultScreen.kt`, dan `ScholarMatchViewModel.kt`. |
| `domain/scholarmatch/` | Rencana. Tempat `MatchInput.kt`, `MatchResult.kt`, dan `ScholarshipMatcher.kt`. Jaga logika matcher sebagai Kotlin yang independen dari UI dan penyimpanan. |

ViewModel memanggil repository atau matcher dan menyiapkan state layar. Teruskan dependensi melalui constructor dan ikuti pola factory yang dipakai di `MainActivity.kt`. Gunakan kontrak `ScholarshipRepository` yang sama untuk semua fitur.

## Mengubah UI

- Ikuti frame Figma yang diberikan untuk tugas tersebut. Cek ukuran, tipografi, warna, jarak, aset, dan perilaku saat berinteraksi. Gunakan kembali tema, resource, dan komponen yang sesuai. Ambil isi beasiswa dari katalog project.
- Detail menampilkan **Ringkasan**, **Persyaratan**, dan **Panduan**. Render hanya subbab tab aktif dan sembunyikan subbab tanpa isi. Gunakan satu kartu teks tanpa ikon per manfaat dan satu kartu per poin dokumen dari `items`.
- Ambil tautan pendaftaran dari `registration.applicationUrl` dan sembunyikan tombol jika tautan kosong. Tampilkan skor relevansi hanya jika detail dibuka dari hasil ScholarMatch.

## Mengubah data

- Gunakan `id` sebagai kunci stabil untuk membuka detail dari daftar. Bedakan `Result.success(null)` untuk ID yang tidak ditemukan dari kegagalan baca atau format.
- Pertahankan kontrak JSON versi 1 kecuali tugas meminta perubahan skema. Jika skema berubah, sesuaikan versi, model, parser, dan pemakai data bersama-sama. Gunakan `preferenceOptions` sebagai sumber pilihan form.
- Pertahankan nilai yang belum tersedia sebagai `null` atau kosong sesuai kontrak. Nominal `null` berarti belum tersedia. Gunakan teks yang jelas, misalnya “Formulir pengajuan dalam bahasa Inggris”, dan pertahankan makna dari sumber.
- Pisahkan status pendaftaran dari evaluasi syarat. Gunakan tanggal, jam jika tersedia, dan zona waktu pendaftaran. `statusAtSnapshot` berlaku per `scheduleAsOf`. Arsip tetap bisa dibuka dari katalog. Rekomendasi default hanya mencakup pendaftaran yang masih berjalan menurut data.

## Aturan ScholarMatch (rencana)

- Gunakan jawaban sementara. Evaluasi `commonRule` lalu nilai setiap jalur di `tracks` secara terpisah. Dukung grup `all` dan `any` serta operator `eq`, `in`, `gt`, `gte`, dan `lte`.
- Jawaban kosong menghasilkan keadaan belum diketahui. Pada `all`, satu syarat gagal membuat grup gagal. Pada `any`, satu alternatif terpenuhi membuat grup terpenuhi.
- Hasil evaluasi adalah **Memenuhi aturan otomatis**, **Tidak memenuhi aturan**, atau **Perlu dicek**. Simpan alasan saat syarat gagal atau input belum lengkap. Jika aturan otomatis terpenuhi dan masih ada `manualChecks` yang berlaku, tampilkan **Perlu dicek** beserta ketentuannya.
- Batasi pilihan ke lima prioritas. Skor awal menghitung prioritas unik yang cocok dengan tag manfaat yang didukung data. Manfaat bersyarat yang belum terverifikasi dan manfaat bersumber sekunder boleh tampil di detail tanpa menambah skor. Tampilkan prioritas yang cocok di hasil.

## Mengerjakan satu tugas

1. Baca kode, data, dan referensi desain yang terkait. Periksa perubahan yang sudah ada dan batasi diff pada fitur yang diminta. Jelaskan kebutuhan perubahan Gradle atau dependensi sebelum mengubahnya.
2. Jalankan pengecekan yang relevan dari root dengan Gradle Wrapper. Konfigurasi modul ada di `app/build.gradle.kts`, katalog versi di `gradle/libs.versions.toml`, dan versi JDK daemon di `gradle/gradle-daemon-jvm.properties`. Siapkan Android SDK 37 sesuai konfigurasi modul.

   ```sh
   ./gradlew :app:assembleDebug :app:testDebugUnitTest
   # Jika ada perangkat atau emulator untuk pengujian instrumentasi
   ./gradlew :app:connectedDebugAndroidTest
   ```

   Di Windows, gunakan `gradlew.bat` dengan nama task yang sama.

3. Untuk UI, jalankan layar pada emulator atau perangkat bila tersedia. Gunakan Compose Preview untuk pemeriksaan tampilan dan bandingkan dengan Figma. Untuk perubahan data, periksa jumlah entri yang diharapkan (saat ini 10), lookup ID, nominal kosong, subbab opsional, dan arsip. Untuk matcher, periksa batas IPK, jawaban kosong, alternatif aturan, dan jalur terpisah.
4. Laporkan perubahan, hasil pengecekan yang benar-benar dijalankan, dan bagian yang belum diuji beserta alasannya.

## Kerja tim

- Bagi pekerjaan per fitur atau perubahan yang bisa direview sendiri. Gunakan satu branch per tugas dari branch dasar yang disepakati. Koordinasikan perubahan yang menyentuh file bersama seperti katalog, navigasi, tema, dan Gradle.
- Saat meminta review, sertakan ringkasan diff, referensi Figma atau ID beasiswa bila relevan, dan hasil pengecekan. Reviewer memeriksa cakupan, perilaku UI dan data, serta dampak pada alur lain sebelum merge.
- Perbarui file ini dalam perubahan yang sama ketika tim menyepakati konvensi baru, mengubah kontrak, atau mewujudkan bagian yang masih ditandai rencana.
