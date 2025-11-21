(() => {
  const container = document.createElement('div');
  container.id = 'bubble-bg';
  document.body.appendChild(container);
  const W = window.innerWidth;
  const count = Math.min(24, Math.max(16, Math.floor(W / 50)));
  function spawn() {
    const b = document.createElement('div');
    b.className = 'bubble';
    const size = 20 + Math.random() * 80;
    const left = Math.random() * 100;
    const dur = 12 + Math.random() * 16;
    const scale = 0.8 + Math.random() * 0.6;
    b.style.width = size + 'px';
    b.style.height = size + 'px';
    b.style.left = left + 'vw';
    b.style.setProperty('--dur', dur + 's');
    b.style.setProperty('--scale', scale);
    const c1 = `rgba(${110 + Math.floor(Math.random()*30)}, ${150 + Math.floor(Math.random()*30)}, 255, ${0.5 + Math.random()*0.3})`;
    const c2 = `rgba(255, ${140 + Math.floor(Math.random()*40)}, ${190 + Math.floor(Math.random()*30)}, ${0.5 + Math.random()*0.3})`;
    b.style.background = `radial-gradient(circle at 30% 30%, ${c1}, ${c2} 60%, rgba(255,255,255,0) 70%)`;
    b.addEventListener('animationend', () => {
      b.remove();
      spawn();
    });
    container.appendChild(b);
  }
  for (let i = 0; i < count; i++) {
    setTimeout(spawn, Math.random() * 3000);
  }
  const start = performance.now();
  function tick(){
    const t = (performance.now() - start) / 1000;
    const s1 = 30 + 20 * (0.5 * (Math.sin(t * 2 * Math.PI / 5) + 1));
    const s2 = 60 + 20 * (0.5 * (Math.sin(t * 2 * Math.PI / 6) + 1));
    document.body.style.setProperty('--stop1', s1.toFixed(2) + '%');
    document.body.style.setProperty('--stop2', s2.toFixed(2) + '%');
    requestAnimationFrame(tick);
  }
  requestAnimationFrame(tick);
})();
