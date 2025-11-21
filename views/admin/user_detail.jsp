<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<html>
<head><title>用户详情（管理员）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/admin">管理员主页</a>
    <a href="<%=request.getContextPath()%>/admin/users">用户管理</a>
    <a href="<%=request.getContextPath()%>/admin/forum">论坛管理</a>
    <a href="<%=request.getContextPath()%>/admin/knowledge">护养知识管理</a>
  </div>
  <div class="right"><a href="<%=request.getContextPath()%>/logout">退出</a></div>
</div>
<div class="page">
<% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); java.util.Map<String,Object> u = (java.util.Map<String,Object>)request.getAttribute("user"); %>
<% if (u != null) { %>
  <% String av = (String)u.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
  <div style="display:flex;align-items:center;gap:16px">
    <img src="<%=avSrc%>" class="avatar" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
    <div>
      <h3><%=u.get("username")%></h3>
      <div>联系方式：<%=u.get("phone")%></div>
      <div>注册时间：<%=fmt.format((java.util.Date)u.get("created_at"))%></div>
    </div>
  </div>
  <hr/>
  <h4>该用户发布的话题</h4>
  <table border="1" cellspacing="0" cellpadding="6">
    <tr><th>ID</th><th>标题</th><th>状态</th><th>发布时间</th></tr>
    <% ResultSet tp = (ResultSet)request.getAttribute("userTopics"); while(tp!=null && tp.next()){ %>
      <tr>
        <td><%=tp.getLong("id")%></td>
        <td><a href="<%=request.getContextPath()%>/admin/forum/detail?id=<%=tp.getLong("id")%>"><%=tp.getString("title")%></a></td>
        <td><span class="<%= "visible".equals(tp.getString("status"))?"status-ok":"status-bad" %>"><%=tp.getString("status")%></span></td>
        <td><%=fmt.format(tp.getTimestamp("created_at"))%></td>
      </tr>
    <% } %>
  </table>
  <hr/>
  <h4>该用户发布的护养知识</h4>
  <table border="1" cellspacing="0" cellpadding="6">
    <tr><th>ID</th><th>标题</th><th>状态</th><th>发布时间</th></tr>
    <% ResultSet ka = (ResultSet)request.getAttribute("userArticles"); while(ka!=null && ka.next()){ %>
      <tr>
        <td><%=ka.getLong("id")%></td>
        <td><a href="<%=request.getContextPath()%>/admin/knowledge/detail?id=<%=ka.getLong("id")%>"><%=ka.getString("title")%></a></td>
        <td><span class="<%= "visible".equals(ka.getString("status"))?"status-ok":"status-bad" %>"><%=ka.getString("status")%></span></td>
        <td><%=fmt.format(ka.getTimestamp("published_at"))%></td>
      </tr>
    <% } %>
  </table>
<% } else { %>
  <div>用户不存在</div>
<% } %>
</div>
</body>
</html>