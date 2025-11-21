<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>文章详情</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user/animals" style="margin-left:auto">添加流浪动物</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
    <a href="<%=request.getContextPath()%>/admin">管理</a>
  </div>
  <div class="right">
    <% if (session.getAttribute("userId") == null) { %>
      <a href="<%=request.getContextPath()%>/login">登录</a>
      <a href="<%=request.getContextPath()%>/register">注册</a>
      <a href="<%=request.getContextPath()%>/login?role=admin">管理员登录</a>
    <% } else { %>
      <% String navAv = (String)session.getAttribute("avatarUrl"); String avatarSrc = null; if (navAv != null && !navAv.isEmpty()) { avatarSrc = (navAv.startsWith("http://")||navAv.startsWith("https://")||navAv.startsWith("/"))?navAv:(request.getContextPath()+"/"+navAv); } else { avatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
      <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
        <a href="#"><img src="<%=avatarSrc%>" class="avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
        <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
      </div>
    <% } %>
  </div>
</div>
<div class="page">
<%
  Map<String,Object> a = (Map<String,Object>)request.getAttribute("article");
  List<Map<String,Object>> comments = (List<Map<String,Object>>)request.getAttribute("comments");
%>
<h3><%=a.get("title")%></h3>
<% java.text.SimpleDateFormat fmtK = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); Object pub=a.get("published_at"); String pubStr; if (pub instanceof java.util.Date) pubStr = fmtK.format((java.util.Date)pub); else if (pub instanceof java.time.LocalDateTime) pubStr = ((java.time.LocalDateTime)pub).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); else { pubStr = String.valueOf(pub); if (pubStr.length()>16) pubStr = pubStr.substring(0,16); } %>
<div>作者：<%=a.get("author")%> 分类：<%=a.get("category")%> 发布时间：<%=pubStr%></div>
<div style="margin:12px 0;white-space:pre-wrap"><%=a.get("content")%></div>
<form method="post" action="<%=request.getContextPath()%>/knowledge" style="display:inline-block;margin-right:8px">
  <input type="hidden" name="action" value="like"/>
  <input type="hidden" name="id" value="<%=a.get("id")%>"/>
  <button type="submit">点赞(<%=a.get("likes_count")%>)</button>
</form>
<% Long userId = (Long)session.getAttribute("userId"); Long adminId = (Long)session.getAttribute("adminId"); Long owner = (Long)a.get("user_id"); if (adminId != null || (userId != null && owner!=null && owner.equals(userId))) { %>
  <form method="post" action="<%=request.getContextPath()%>/knowledge" style="display:inline-block">
    <input type="hidden" name="action" value="delete"/>
    <input type="hidden" name="id" value="<%=a.get("id")%>"/>
    <button type="submit">删除</button>
  </form>
<% } %>
<hr/>
<h4>评论</h4>
<form method="post" action="<%=request.getContextPath()%>/knowledge">
  <input type="hidden" name="action" value="comment"/>
  <input type="hidden" name="id" value="<%=a.get("id")%>"/>
  <div class="form-row"><label>内容</label><input name="content"/></div>
  <div class="form-actions"><button type="submit">发表评论</button></div>
</form>
<% if (comments != null) for (Map<String,Object> c: comments) { %>
  <% Object ct=c.get("created_at"); String ctStr; if (ct instanceof java.util.Date) ctStr = fmtK.format((java.util.Date)ct); else if (ct instanceof java.time.LocalDateTime) ctStr = ((java.time.LocalDateTime)ct).format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")); else { ctStr = String.valueOf(ct); if (ctStr.length()>16) ctStr = ctStr.substring(0,16); } %>
  <div style="padding:6px 0;border-top:1px dashed #eee"><b><%=c.get("username")%>：</b><%=c.get("content")%> <span style="color:#999"><%=ctStr%></span></div>
<% } %>
</div>
</body>
</html>
