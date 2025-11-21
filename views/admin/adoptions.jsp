<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<html>
<head><title>领养信息管理</title>
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
<% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<h3>已处理的领养申请（仅查看）</h3>
<table border="1" cellspacing="0" cellpadding="6">
  <tr><th>ID</th><th>用户</th><th>动物</th><th>状态</th><th>提交时间</th></tr>
  <% ResultSet done = (ResultSet)request.getAttribute("done"); while (done != null && done.next()) { %>
    <tr>
      <td><%=done.getLong("id")%></td>
      <td><%=done.getString("username")%></td>
      <td><a href="<%=request.getContextPath()%>/animals?from=admin&id=<%=done.getLong("animal_id")%>"><%=done.getString("name")%></a></td>
      <td><%=done.getString("status")%></td>
      <td><%=fmt.format(done.getTimestamp("created_at"))%></td>
    </tr>
  <% } %>
 </table>
</div>
</body>
</html>
<h3>待处理的领养申请</h3>
<table border="1" cellspacing="0" cellpadding="6">
  <tr><th>ID</th><th>用户</th><th>动物</th><th>状态</th><th>提交时间</th></tr>
  <% ResultSet pending = (ResultSet)request.getAttribute("pending"); while (pending != null && pending.next()) { %>
    <tr>
      <td><%=pending.getLong("id")%></td>
      <td><%=pending.getString("username")%></td>
      <td><a href="<%=request.getContextPath()%>/animals?from=admin&id=<%=pending.getLong("animal_id")%>"><%=pending.getString("name")%></a></td>
      <td><%=pending.getString("status")%></td>
      <td><%=fmt.format(pending.getTimestamp("created_at"))%></td>
    </tr>
  <% } %>
</table>
