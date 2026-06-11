const adminAvatar   = document.getElementById("adminAvatar");
const adminDropdown = document.getElementById("adminDropdown");
const adminWrapper  = document.getElementById("adminWrapper");

// Abre/fecha ao clicar no avatar
adminAvatar.addEventListener("click", function (e) {
    e.stopPropagation(); // evita que o clique "vaze" pro document e feche imediatamente
    adminDropdown.classList.toggle("open");
});

// Fecha ao clicar em qualquer lugar fora do menu
document.addEventListener("click", function (e) {
    if (!adminWrapper.contains(e.target)) {
        adminDropdown.classList.remove("open");
    }
});

/* Abre e fecha o menu admin ao clicar no avatar.
 Fecha também se clicar fora do menu. */