document.addEventListener('DOMContentLoaded',()=>{
  document.querySelectorAll('input[data-image-url]').forEach(inp=>{
    const name = inp.getAttribute('data-name') || 'photo_file';
    let file = document.createElement('input');
    file.type='file'; file.accept='image/*'; file.name=name; file.style.display='none';
    inp.parentNode.insertBefore(file, inp.nextSibling);
    const preview = document.createElement('img'); preview.style.maxWidth='260px'; preview.style.display='none'; preview.style.marginTop='6px';
    inp.parentNode.insertBefore(preview, file.nextSibling);
    function openPicker(){ file.click(); }
    inp.addEventListener('focus', openPicker);
    inp.addEventListener('click', openPicker);
    file.addEventListener('change', ()=>{
      const f=file.files[0]; if(!f) return; const url=URL.createObjectURL(f); inp.value=url; preview.src=url; preview.style.display='block';
    });
  });
});
