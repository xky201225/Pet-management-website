(() => {
  function setup(container) {
    if (!container) return;
    const img = container.querySelector('img');
    const pop = container.querySelector('.logout-pop');
    const userUrl = container.dataset.userUrl || (window.__ctx || '') + '/user';
    if (!img || !pop) return;

    container.addEventListener('mouseenter', () => { pop.style.display = 'flex'; });
    container.addEventListener('mouseleave', () => { if (!container.classList.contains('open')) pop.style.display = 'none'; });
    img.addEventListener('click', (e) => { e.preventDefault(); e.stopPropagation(); container.classList.toggle('open'); if (container.classList.contains('open')) { pop.style.display = 'flex'; } else { pop.style.display = 'none'; } });
    // dblclick 进入个人中心的逻辑移除，改为在弹层中提供选项
    document.addEventListener('click', (e) => { if (container.classList.contains('open') && !container.contains(e.target)) { container.classList.remove('open'); pop.style.display = 'none'; } });
  }
  document.addEventListener('DOMContentLoaded', () => { document.querySelectorAll('.nav-avatar').forEach(setup); });
})();