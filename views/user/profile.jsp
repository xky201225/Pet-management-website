<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>用户详情</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
  </div>
  <div class="right"></div>
</div>
<div class="page">
<% Map<String,Object> u = (Map<String,Object>)request.getAttribute("user"); %>
<% if (u != null) { %>
  <% String av = (String)u.get("avatar_url"); String avatarSrc = (av!=null && (av.startsWith("http")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
  <div style="display:flex;align-items:center;gap:16px">
    <img src="<%=avatarSrc%>" class="avatar" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
    <div>
      <h3><%=u.get("username")%></h3>
      <div>联系方式：<%=u.get("phone")%></div>
      <% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
      <div>注册时间：<%=fmt.format((java.util.Date)u.get("created_at"))%></div>
    </div>
  </div>
  <hr/>
  <div>添加流浪动物申请数：<%=request.getAttribute("addCount")%></div>
  <div>已批准的领养申请数：<%=request.getAttribute("adoptOk")%></div>
  <hr/>
  <h4>该用户发布的话题</h4>
  <% List<Map<String,Object>> topics = (List<Map<String,Object>>)request.getAttribute("topics"); %>
  <% if (topics != null) for (Map<String,Object> t: topics) { %>
    <div><a href="<%=request.getContextPath()%>/forum?id=<%=t.get("id")%>"><%=t.get("title")%></a> · <small><%=fmt.format((java.util.Date)t.get("created_at"))%></small></div>
  <% } %>
  <hr/>
  <h4>该用户发布的护养知识</h4>
  <% List<Map<String,Object>> as = (List<Map<String,Object>>)request.getAttribute("articles"); %>
  <% if (as != null) for (Map<String,Object> a: as) { %>
    <div><a href="<%=request.getContextPath()%>/knowledge?id=<%=a.get("id")%>"><%=a.get("title")%></a> · <small><%=fmt.format((java.util.Date)a.get("published_at"))%></small></div>
  <% } %>
<% } else { %>
  <div>用户不存在</div>
<% } %>
</div>
</body>
</html>