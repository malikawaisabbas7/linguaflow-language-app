# Adding Poppins Fonts

Android resource folders (like `res/font`) cannot contain non-resource files,
so font setup instructions live here instead.

Download Poppins (free, Google Fonts): https://fonts.google.com/specimen/Poppins

Place these 4 files into `app/src/main/res/font/`:
- poppins_regular.ttf
- poppins_medium.ttf
- poppins_semibold.ttf
- poppins_bold.ttf

These names must match the `Font(R.font.poppins_regular, ...)` references in
`app/src/main/java/com/linguaflow/app/theme/Type.kt`. Until you add them, the
project will fail to build because `R.font.poppins_*` won't resolve — this is
expected for a fresh checkout and is a one-time setup step.
