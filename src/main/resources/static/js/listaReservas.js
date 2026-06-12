function toggleDetalhes(btn) {
    
    // O botão está dentro de .reserva-corpo, que está dentro de .reserva-card
    const card = btn.closest(".reserva-card");
    const detalhes = card.querySelector(".reserva-detalhes");

    const estaAberto = detalhes.style.display !== "none";

    if (estaAberto) {
        detalhes.style.display = "none";
        btn.classList.remove("aberto");
    } else {
        detalhes.style.display = "block";
        btn.classList.add("aberto");
    }
}