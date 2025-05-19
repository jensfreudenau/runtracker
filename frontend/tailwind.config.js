/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
        "./src/main/resources/templates/**/*.html",
        "./src/main/resources/static/**/*.{js,css}", // Optional: falls du auch JS-Dateien mit Tailwind-Klassen hast
    ],
    theme: {
        extend: {},
    },
    plugins: [],
}
