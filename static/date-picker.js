document.addEventListener('DOMContentLoaded',()=>{
  document.querySelectorAll('input[data-date-picker]').forEach(inp=>{
    inp.setAttribute('readonly','readonly');
    const hidden = document.createElement('input');
    hidden.type = 'date';
    hidden.style.display = 'none';
    inp.parentNode.insertBefore(hidden, inp.nextSibling);
    function open() { hidden.showPicker ? hidden.showPicker() : hidden.click(); }
    inp.addEventListener('focus', open);
    inp.addEventListener('click', open);
    hidden.addEventListener('change', ()=>{
      if (!hidden.value) return;
      const [y,m,d] = hidden.value.split('-');
      inp.value = `${y}.${parseInt(m,10)}.${parseInt(d,10)}`;
    });
  });
});