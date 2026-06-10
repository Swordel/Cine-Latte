const selectStatus = document.getElementById("status");
const grupoEmBreve = document.getElementById("grupo-em-breve");
const grupoImagens = document.getElementById("grupo-imagens");
const btnSubmit = document.getElementById("btn-submit");
const inputClassif  = document.getElementById("classificacao");
const optIndefinida = document.getElementById("opt-indefinida");

selectStatus.addEventListener("change", function () {
    const status = this.value;

    if (status === "EM_CARTAZ") {
        esconder(grupoEmBreve);
        optIndefinida.style.display = "none";
        
        if (inputClassif.value === "?")
            inputClassif.value = "L";

    }
    else if (status === "EM_BREVE") {
        mostrar(grupoEmBreve);
        optIndefinida.style.display = "";
        inputClassif.value = "?";
    }

    mostrar(grupoImagens);
    mostrar(btnSubmit);
});

function mostrar(el) { el.style.display = ""; }
function esconder(el) { el.style.display = "none"; }