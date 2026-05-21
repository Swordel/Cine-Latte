// ── HERO CAROUSEL ──────────────────────────────────────────
const heroSlides = document.querySelectorAll('.hero-slide');
const heroDots   = document.getElementById('heroDots');
let heroIndex    = 0;
let heroTimer;
 
heroSlides.forEach((_, i) => {     // Cria os dots dinamicamente
    const dot = document.createElement('span');
    dot.classList.add('dot');
    if (i === 0) dot.classList.add('active');
    dot.addEventListener('click', () => goHero(i));
    heroDots.appendChild(dot);
});
 
function goHero(n) {
    heroSlides[heroIndex].classList.remove('active');
    heroDots.children[heroIndex].classList.remove('active');
    heroIndex = (n + heroSlides.length) % heroSlides.length;
    heroSlides[heroIndex].classList.add('active');
    heroDots.children[heroIndex].classList.add('active');
    resetHeroTimer();
}
 
function resetHeroTimer() {
    clearInterval(heroTimer);
    heroTimer = setInterval(() => goHero(heroIndex + 1), 5000);
}
 
document.getElementById('heroPrev').addEventListener('click', () => goHero(heroIndex - 1));
document.getElementById('heroNext').addEventListener('click', () => goHero(heroIndex + 1));
resetHeroTimer();

// ── CARROSSEIS HORIZONTAIS (Em Cartaz / Em Breve) ────────────
//
// Lógica das setas inteligentes:
//   - Seta esquerda  → aparece só quando scrollLeft > 0            (classe "can-prev")
//   - Seta direita   → aparece só quando há conteúdo à direita     (classe "can-next")
// O CSS usa essas classes para exibir as setas e as faixas escuras.

function setupHScroll(trackId, wrapperClass, prevId, nextId) {
    const track   = document.getElementById(trackId);
    const wrapper = document.querySelector(wrapperClass);
    const step    = 600;

    function updateButtons() {
        const atStart = track.scrollLeft <= 0;
        const atEnd   = track.scrollLeft + track.clientWidth >= track.scrollWidth - 1;

        wrapper.classList.toggle('can-prev', !atStart);
        wrapper.classList.toggle('can-next', !atEnd);
    }

     // Chama de todas as formas para garantir
    updateButtons();
    setTimeout(updateButtons, 100);
    setTimeout(updateButtons, 500);
    window.addEventListener('load', updateButtons);
    
    // Atualiza ao rolar
    track.addEventListener('scroll', updateButtons);

    // Botões
    document.getElementById(prevId).addEventListener('click', () => {
        track.scrollBy({ left: -step, behavior: 'smooth' });
    });
    document.getElementById(nextId).addEventListener('click', () => {
        track.scrollBy({ left: step, behavior: 'smooth' });
    });
}

setupHScroll('cartazTrack', '#cartazWrapper', 'cartazPrev', 'cartazNext');
setupHScroll('breveTrack',  '#breveWrapper',  'brevePrev',  'breveNext');