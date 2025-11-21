<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>管理员首页</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/admin">管理员主页</a>
    <a href="<%=request.getContextPath()%>/admin/users">用户管理</a>
    <a href="<%=request.getContextPath()%>/admin/animals">动物管理</a>
    <a href="<%=request.getContextPath()%>/admin/adoptions">领养管理</a>
    <a href="<%=request.getContextPath()%>/admin/forum">论坛管理</a>
    <a href="<%=request.getContextPath()%>/admin/knowledge">护养知识管理</a>
    <a href="<%=request.getContextPath()%>/admin/review">待处理审核</a>
  </div>
  <div class="right">
    <a href="<%=request.getContextPath()%>/logout">退出</a>
  </div>
</div>
<div class="page">
<h3>数据概览</h3>
<div>注册用户数：<%=request.getAttribute("users")%></div>
<div>待领养动物数：<%=request.getAttribute("animals")%></div>
<div>待审核申请数：<%=request.getAttribute("appsPending")%></div>
<div>待审核添加动物数：<%=request.getAttribute("pendingSubs")%></div>
<hr/>
</div>
</body>
</html>
