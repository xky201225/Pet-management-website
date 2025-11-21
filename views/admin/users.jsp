<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<html>
<head><title>用户管理</title>
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
<h3>用户列表</h3>
<table border="1" cellspacing="0" cellpadding="6">
  <tr><th>ID</th><th>用户名</th><th>联系方式</th><th>状态</th><th>注册时间</th><th>操作</th></tr>
  <% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); ResultSet rs = (ResultSet)request.getAttribute("rs"); while (rs != null && rs.next()) { %>
    <tr>
      <td><%=rs.getLong("id")%></td>
      <td><a href="<%=request.getContextPath()%>/admin/user?id=<%=rs.getLong("id")%>"><%=rs.getString("username")%></a></td>
      <td><%=rs.getString("phone")%></td>
      <td><span class="<%= "banned".equals(rs.getString("status"))?"status-bad":"status-ok" %>"><%=rs.getString("status")%></span></td>
      <td><%=fmt.format(rs.getTimestamp("created_at"))%></td>
      <td>
        <form method="post" action="<%=request.getContextPath()%>/admin/users" style="display:inline">
          <input type="hidden" name="action" value="banUser"/>
          <input type="hidden" name="id" value="<%=rs.getLong("id")%>"/>
          <button type="submit">禁用</button>
        </form>
        <form method="post" action="<%=request.getContextPath()%>/admin/users" style="display:inline">
          <input type="hidden" name="action" value="unbanUser"/>
          <input type="hidden" name="id" value="<%=rs.getLong("id")%>"/>
          <button type="submit">解封</button>
        </form>
      </td>
    </tr>
  <% } %>
 </table>
</div>
</body>
</html>
