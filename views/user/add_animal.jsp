<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>添加流浪动物</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
  </div>
  <div class="right">
    <% String navAv = (String)session.getAttribute("avatarUrl"); String avatarSrc = null; if (navAv != null && !navAv.isEmpty()) { avatarSrc = (navAv.startsWith("http://")||navAv.startsWith("https://")||navAv.startsWith("/"))?navAv:(request.getContextPath()+"/"+navAv); } else { avatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
    <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
      <a href="#"><img src="<%=avatarSrc%>" class="avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
      <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
    </div>
  </div>
</div>
<div class="page">
<h3>添加流浪动物（需管理员审核）</h3>
<% String msg = (String)request.getAttribute("msg"); if (msg != null) { %>
<div style="color:#5b8fe6"><%=msg%></div>
<% } %>
<form method="post" action="<%=request.getContextPath()%>/user/animals" enctype="multipart/form-data">
  <div class="form-row"><label>名称</label><input name="name"/></div>
  <div class="form-row"><label>物种</label><input name="species"/></div>
  <div class="form-row"><label>品种</label><input name="breed"/></div>
  <div class="form-row"><label>年龄</label><input name="age"/></div>
  <div class="form-row"><label>健康状态</label><input name="health_status"/></div>
  <div class="form-row"><label>救助地点</label><input name="rescue_location"/></div>
  <div class="form-row"><label>救助时间</label><input name="rescue_time" data-date-picker="1" required placeholder="YYYY.MM.DD"/><span class="date-tip">点击选择日期</span></div>
  <div class="form-row"><label>照片URL</label><input name="photo_url" data-image-url="1" data-name="photo_file" required/></div>
  <div class="form-row"><label>描述</label><input name="description"/></div>
  <div class="form-actions"><button type="submit">提交审核</button></div>
</form>
</div>
<script src="<%=request.getContextPath()%>/static/image-picker.js"></script>
<script src="<%=request.getContextPath()%>/static/date-picker.js"></script>
</body>
</html>
