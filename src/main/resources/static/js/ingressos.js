let qtdInteira = 0;
let qtdMeia    = 0;

// == CONTADORES ==
document.getElementById('btnPlusInteira').addEventListener('click', () => {
    if (qtdInteira + qtdMeia < TOTAL_ASSENTOS) {
        qtdInteira++;
        atualizar();
    }
});

document.getElementById('btnMinusInteira').addEventListener('click', () => {
    if (qtdInteira > 0) {
        qtdInteira--;
        atualizar();
    }
});

document.getElementById('btnPlusMeia').addEventListener('click', () => {
    if (qtdInteira + qtdMeia < TOTAL_ASSENTOS) {
        qtdMeia++;
        atualizar();
    }
});

document.getElementById('btnMinusMeia').addEventListener('click', () => {
    if (qtdMeia > 0) {
        qtdMeia--;
        atualizar();
    }
});

// ── ATUALIZAÇÃO DO RESUMO ────────────────────────────────────
function atualizar() {
    const total = qtdInteira + qtdMeia;
    const valor = (qtdInteira * PRECO_INTEIRA) + (qtdMeia * PRECO_MEIA);
    const valorFmt = 'R$ ' + valor.toFixed(2).replace('.', ',');

    // Contadores visuais
    document.getElementById('valInteira').textContent = qtdInteira;
    document.getElementById('valMeia').textContent    = qtdMeia;

    // Resumo lateral — Inteira
    const rowInteira = document.getElementById('resumoInteiraRow');
    if (qtdInteira > 0) {
        rowInteira.style.display = 'flex';
        document.getElementById('resumoInteiraLabel').textContent =
            qtdInteira + 'x Inteira';
        document.getElementById('resumoInteiraValor').textContent =
            'R$ ' + (qtdInteira * PRECO_INTEIRA).toFixed(2).replace('.', ',');
    } else {
        rowInteira.style.display = 'none';
    }

    // Resumo lateral — Meia
    const rowMeia = document.getElementById('resumoMeiaRow');
    if (qtdMeia > 0) {
        rowMeia.style.display = 'flex';
        document.getElementById('resumoMeiaLabel').textContent =
            qtdMeia + 'x Meia-entrada';
        document.getElementById('resumoMeiaValor').textContent =
            'R$ ' + (qtdMeia * PRECO_MEIA).toFixed(2).replace('.', ',');
    } else {
        rowMeia.style.display = 'none';
    }

    // Total
    document.getElementById('resumoTotal').textContent = valorFmt;

    // Botão habilitado apenas quando total = número de assentos
    document.getElementById('btnContinuar').disabled = (total !== TOTAL_ASSENTOS);

    // Destaque visual no card quando tem ingressos selecionados
    document.getElementById('cardInteira').classList.toggle('ativo', qtdInteira > 0);
    document.getElementById('cardMeia').classList.toggle('ativo', qtdMeia > 0);
}

document.getElementById('btnContinuar').addEventListener('click', () => {

    const total = qtdInteira + qtdMeia;

    if (total !== TOTAL_ASSENTOS)
        return;

    const form = document.createElement('form');

    form.method = 'POST';
    form.action = window.location.pathname;

    const inteira = document.createElement('input');
    inteira.type = 'hidden';
    inteira.name = 'qtdInteira';
    inteira.value = qtdInteira;

    const meia = document.createElement('input');
    meia.type = 'hidden';
    meia.name = 'qtdMeia';
    meia.value = qtdMeia;

    form.appendChild(inteira);
    form.appendChild(meia);

    document.body.appendChild(form);
    form.submit();
});