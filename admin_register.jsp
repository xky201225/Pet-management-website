<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>管理员注册</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
  </div>
  <div class="right">
    <a href="<%=request.getContextPath()%>/login?role=admin">管理员登录</a>
  </div>
</div>
<div class="page">
<% int rnd = 1 + (int)(Math.random()*6); String pic = request.getContextPath()+"/animalsPicture/"+rnd+".jpg"; %>
<div class="card" style="display:flex;gap:24px;align-items:center">
  <div style="flex:1;min-width:320px">
    <h3>管理员注册</h3>
    <% String error = (String)request.getAttribute("error"); if (error != null) { %>
      <div style="color:#ff4d88;margin-bottom:8px"><%=error%></div>
    <% } %>
    <form method="post" action="<%=request.getContextPath()%>/admin/register">
      <div class="form-row"><label>用户名</label><input name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>" required/></div>
      <div class="form-row"><label>联系方式</label><input name="phone" value="<%=request.getParameter("phone")==null?"":request.getParameter("phone")%>" required/><span class="input-tip" id="tip-phone" style="display:none">必须为11位数字</span></div>
      <div class="form-row"><label>密码</label><input type="password" name="password" value="<%=request.getParameter("password")==null?"":request.getParameter("password")%>" required/><span class="input-tip" id="tip-pwd" style="display:none">至少6位且包含字母和数字</span></div>
      <div class="form-row"><label>确认密码</label><input type="password" name="confirm" value="<%=request.getParameter("confirm")==null?"":request.getParameter("confirm")%>" required/></div>
      <div class="form-row"><label>邀请码</label><input name="code" value="<%=request.getParameter("code")==null?"":request.getParameter("code")%>" placeholder="输入邀请码" required/></div>
      <div class="form-actions"><button type="submit">注册</button></div>
    </form>
  </div>
  <div style="flex:1;display:flex;justify-content:center"><img src="<%=pic%>" alt="pet" style="max-width:100%;border-radius:16px" onerror="this.style.display='none'"/></div>
</div>
<script>
  const phone = document.querySelector('input[name=phone]');
  const pwd = document.querySelector('input[name=password]');
  function vPhone(){ const ok = /^\d{11}$/.test(phone.value); document.getElementById('tip-phone').style.display = ok?'none':'inline'; }
  function vPwd(){ const v = pwd.value; const ok = v.length>=6 && /[A-Za-z]/.test(v) && /\d/.test(v); document.getElementById('tip-pwd').style.display = ok?'none':'inline'; }
  phone && phone.addEventListener('input', vPhone); pwd && pwd.addEventListener('input', vPwd);
  vPhone(); vPwd();
</script>
</div>
</body>
</html>
