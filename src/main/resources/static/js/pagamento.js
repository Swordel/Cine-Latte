// ── SELEÇÃO DE FORMA DE PAGAMENTO ────────────────────────────
function selecionarForma(forma) {
    // Atualiza o campo oculto que será enviado ao backend
    document.getElementById('inputFormaPagamento').value = forma;

    // Troca o tab ativo
    document.getElementById('tabCartao').classList.toggle('active', forma === 'CARTAO');
    document.getElementById('tabPix').classList.toggle('active',    forma === 'PIX');

    // Mostra o painel correto
    document.getElementById('painelCartao').classList.toggle('hidden', forma !== 'CARTAO');
    document.getElementById('painelPix').classList.toggle('hidden',    forma !== 'PIX');
}

// ── FORMATAÇÃO DO NÚMERO DO CARTÃO ───────────────────────────
// Insere espaço a cada 4 dígitos: "1234 5678 9012 3456"
function formatarCartao(input) {
    let valor = input.value.replace(/\D/g, '');           // só números
    valor = valor.substring(0, 16);                        // máximo 16 dígitos
    valor = valor.replace(/(\d{4})(?=\d)/g, '$1 ');       // insere espaço
    input.value = valor;
}

// ── FORMATAÇÃO DA VALIDADE ────────────────────────────────────
// Formato: "MM/AA"
function formatarValidade(input) {
    let valor = input.value.replace(/\D/g, '');
    valor = valor.substring(0, 4);
    if (valor.length > 2) {
        valor = valor.substring(0, 2) + '/' + valor.substring(2);
    }
    input.value = valor;
}