// ── LEITURA DOS DADOS DO BANCO ───────────────────────────────
// Thymeleaf gerou um <span> oculto para cada assento.
// Lemos os data-attributes e organizamos por fileira.

const spans = document.querySelectorAll('#assentosDados span');
const assentosPorFileira = {};

spans.forEach(span => {
    const a = {
        id:      span.dataset.id,
        codigo:  span.dataset.codigo,
        fileira: span.dataset.fileira,
        numero:  parseInt(span.dataset.numero),
        tipo:    span.dataset.tipo,
        ocupado: span.dataset.ocupado === 'true'
    };
    if (!assentosPorFileira[a.fileira]) {
        assentosPorFileira[a.fileira] = [];
    }
    assentosPorFileira[a.fileira].push(a);
});

// ── SELECIONADOS ───────────────────────────────────────────────────
// Um Set é parecido com uma lista, mas não permite duplicados.
// códigos selecionados: "A1", "C5"...
const selecionados = new Set();

// ── GERAÇÃO DO MAPA ──────────────────────────────────────────
// Ordem: L no topo, A na base (próxima à tela).
// Espaçamento extra entre H/G e entre C/B.

function gerarSala() {
    const sala = document.getElementById('sala');
    sala.innerHTML = '';

    renderGrupo(sala, ['L','K','J','I','H']);
    sala.appendChild(criarSpacer());
    renderGrupo(sala, ['G','F','E','D','C']);
    sala.appendChild(criarSpacer());
    renderGrupo(sala, ['B','A']);
}

function renderGrupo(sala, fileiras) {
    fileiras.forEach(f => sala.appendChild(buildFileira(f)));
}

function criarSpacer() {
    const s = document.createElement('div');
    s.classList.add('fileira-spacer');
    return s;
}

// ── LAYOUT POR FILEIRA ────────────────────────────────────────
// Cada "slot" (assento ou gap) tem a mesma largura → alinhamento garantido.
// Total de slots por fileira = 28.
//   L     → [28] corridos
//   A, B  → [4] gap×3 [14] gap×3 [4]          = 4+3+14+3+4  = 28
//   C     → [3] gap×5 [12] gap×8               = 3+5+12+8    = 28
//   D–G   → [3] gap×3 [16] gap×6               = 3+3+16+6    = 28
//   H–K   → [3] gap×3 [16] gap×3 [3]           = 3+3+16+3+3  = 28

function buildFileira(fileira) {
    const assentos = assentosPorFileira[fileira] || [];
    const row = document.createElement('div');
    row.classList.add('fileira');

    row.appendChild(criarLabel(fileira)); // label esquerda

    // O cursor indica qual assento está sendo usado, ou seja, controla qual assento da lista está sendo usado.
    let cursor = 0;

    // Adiciona `n` assentos a partir do cursor
    function seat(n) {
        for (let i = 0; i < n && cursor < assentos.length; i++, cursor++) {
            row.appendChild(criarBotao(assentos[cursor]));
        }
    }

    // Adiciona `n` gaps (espaços vazios do mesmo tamanho que um assento)
    function gap(n) {
        for (let i = 0; i < n; i++) {
            const g = document.createElement('div');
            g.classList.add('seat-gap');
            row.appendChild(g);
        }
    }

    if      (fileira === 'L')                        { seat(28); }
    else if (fileira <= 'B')                         { seat(4);  gap(3); seat(14); gap(3); seat(4); }
    else if (fileira === 'C')                        { seat(3);  gap(5); seat(12); gap(8); }
    else if (fileira >= 'D' && fileira <= 'G')       { seat(3);  gap(3); seat(16); gap(6); }
    else /* H–K */                                   { seat(3);  gap(3); seat(16); gap(3); seat(3); }

    row.appendChild(criarLabel(fileira)); // label direita
    return row;
}

function criarLabel(fileira) {
    const label = document.createElement('span');
    label.classList.add('fileira-label');
    label.textContent = fileira;
    return label;
}

// ── BOTÃO DE ASSENTO ─────────────────────────────────────────
function criarBotao(assento) {
    const btn = document.createElement('button');
    btn.classList.add('seat', assento.ocupado ? 'ocupado' : assento.tipo);
    btn.dataset.codigo = assento.codigo;
    btn.dataset.tipo   = assento.tipo;
    btn.dataset.id     = assento.id; // id do banco, necessário para a reserva
    btn.textContent    = assento.codigo;
    btn.setAttribute('title', assento.codigo);
    btn.setAttribute('aria-label', 'Assento ' + assento.codigo);
    if (!assento.ocupado) {
        btn.addEventListener('click', () => toggleAssento(btn));
    }
    return btn;
}

// ── SELEÇÃO ───────────────────────────────────────────────────
function toggleAssento(btn) {
    const codigo = btn.dataset.codigo;
    const tipo   = btn.dataset.tipo;
    if (selecionados.has(codigo)) {
        selecionados.delete(codigo);
        btn.classList.replace('selecionado', tipo);
    } else {
        selecionados.add(codigo);
        btn.classList.replace(tipo, 'selecionado');
    }
    atualizarBarra();
}

function atualizarBarra() {
    const lista = document.getElementById('selectedList');
    const botao = document.getElementById('btnContinuar');
    if (selecionados.size === 0) {
        lista.textContent = '—';
        botao.disabled = true;
    } else {
        const ordenados = [...selecionados].sort((a, b) => {
            if (a[0] !== b[0]) return a[0].localeCompare(b[0]);
            return parseInt(a.slice(1)) - parseInt(b.slice(1));
        });
        lista.textContent = ordenados.join('  ·  ');
        botao.disabled = false;
    }
}

// ── TOGGLE NUMERAÇÃO ──────────────────────────────────────────
document.getElementById('toggleNumeros').addEventListener('click', function () {
    this.classList.toggle('on');
    document.body.classList.toggle('show-numbers');
});

/* É como fazer
 <body class="show-numbers">
    <button class="seat">A1</button>
    <button class="seat">A2</button>
    <button class="seat">A3</button>
 </body>
*/

// ── BOTÃO CONTINUAR: submete formulário POST ─────────────────
// Campos ocultos são gerados dinamicamente e submetidos via form
// Isso evita a URL feia com ?assentoIds=5&assentoIds=12...

document.getElementById('btnContinuar').addEventListener('click', () => {
    if (selecionados.size === 0)
        return;
 
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = '/sessao/' + SESSAO_ID + '/assentos';
 
    // Adiciona um campo oculto por assento selecionado
    document.querySelectorAll('.seat.selecionado').forEach(btn => {
        const input = document.createElement('input');
        input.type  = 'hidden';
        input.name  = 'assentoIds';
        input.value = btn.dataset.id;
        form.appendChild(input);
    });
 
    document.body.appendChild(form);
    form.submit();
});

// ── INÍCIO ────────────────────────────────────────────────────
gerarSala();
