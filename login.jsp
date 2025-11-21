<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>登录</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
  </div>
  <div class="right">
    <a href="<%=request.getContextPath()%>/register">注册</a>
  </div>
</div>
<div class="page">
<% int rnd = 1 + (int)(Math.random()*6); String pic = request.getContextPath()+"/animalsPicture/"+rnd+".jpg"; %>
<div class="card" style="display:flex;gap:24px;align-items:center">
  <div style="flex:1;min-width:320px">
    <h3>登录</h3>
    <% String error = (String)request.getAttribute("error"); if (error != null) { %>
      <div style="color:#ff4d88"><%=error%></div>
    <% } %>
    <form method="post" action="<%=request.getContextPath()%>/login">
      <div class="form-row">
        <label>身份</label>
        <select name="role">
          <option value="user">用户</option>
          <option value="admin" <%= "admin".equals(request.getParameter("role")) ? "selected" : "" %>>管理员</option>
        </select>
      </div>
      <div class="form-row"><label>用户名</label><input name="username" value="<%=request.getParameter("username")==null?"":request.getParameter("username")%>" required/></div>
      <div class="form-row"><label>密码</label><input type="password" name="password" value="<%=request.getParameter("password")==null?"":request.getParameter("password")%>" required/></div>
      <div class="form-row"><label>验证码</label>
        <input name="captcha" required/>
        <img src="<%=request.getContextPath()%>/captcha" alt="captcha" style="margin-left:8px;vertical-align:middle;height:36px;border-radius:8px" onclick="this.src='<%=request.getContextPath()%>/captcha?ts='+Date.now()"/>
      </div>
      <div class="form-actions"><button type="submit">登录</button></div>
    </form>
    <div style="margin-top:12px">没有账号？<a href="<%=request.getContextPath()%>/register">用户注册</a></div>
    <div style="margin-top:6px">管理员没有账号？<a href="<%=request.getContextPath()%>/admin/register">管理员注册</a>（需邀请码）</div>
  </div>
  <div style="flex:1;display:flex;justify-content:center"><img src="<%=pic%>" alt="pet" style="max-width:100%;border-radius:16px" onerror="this.style.display='none'"/></div>
</div>
</div>
</body>
</html>
