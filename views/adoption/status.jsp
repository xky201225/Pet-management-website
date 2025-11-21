<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>收养申请进度</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
  </div>
  <div class="right">
    <a href="<%=request.getContextPath()%>/logout">退出</a>
  </div>
</div>
<div class="page">
<h3>我的收养申请</h3>
<% List<Map<String,Object>> apps = (List<Map<String,Object>>)request.getAttribute("applications"); %>
<table border="1" cellspacing="0" cellpadding="6">
  <tr><th>ID</th><th>动物</th><th>状态</th><th>审核意见</th><th>提交时间</th></tr>
  <% if (apps != null) for (Map<String,Object> m: apps) { %>
    <tr>
      <td><%=m.get("id")%></td>
      <td><%=m.get("animal_name")%></td>
      <td><%=m.get("status")%></td>
      <td><%=m.get("review_opinion")%></td>
      <td><%=m.get("created_at")%></td>
    </tr>
  <% } %>
  <% if (apps == null || apps.isEmpty()) { %>
    <tr><td colspan="5">暂无申请</td></tr>
  <% } %>
 </table>
</div>
</body>
</html>
