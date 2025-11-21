<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>护养知识详情（管理员）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/admin">管理员主页</a>
    <a href="<%=request.getContextPath()%>/admin/knowledge">护养知识管理</a>
  </div>
  <div class="right"><a href="<%=request.getContextPath()%>/logout">退出</a></div>
</div>
<div class="page">
<% Map<String,Object> a = (Map<String,Object>)request.getAttribute("article"); java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<% if (a != null) { %>
  <h3><%=a.get("title")%></h3>
  <div style="display:flex;align-items:center;gap:8px;color:#6e6e73">
    <% String av = (String)a.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
    <img src="<%=avSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
    <span><%=a.get("username")%></span>
    <small><%=fmt.format((java.util.Date)a.get("published_at"))%></small>
  </div>
  <div style="margin-top:12px;white-space:pre-wrap"><%=a.get("content")%></div>
  <div style="margin-top:8px;color:#6e6e73">赞 <%=a.get("likes_count")%> · 评论 <%=a.get("comments_count")%> · 状态：<span class="<%= "visible".equals(a.get("status"))?"status-ok":"status-bad" %>"><%=a.get("status")%></span></div>
  <hr/>
  <form method="post" action="<%=request.getContextPath()%>/admin/knowledge" style="display:inline-block;margin-right:8px">
    <input type="hidden" name="action" value="hideArticle"/>
    <input type="hidden" name="id" value="<%=a.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <button type="submit">屏蔽</button>
  </form>
  <form method="post" action="<%=request.getContextPath()%>/admin/knowledge" style="display:inline-block;margin-right:8px">
    <input type="hidden" name="action" value="showArticle"/>
    <input type="hidden" name="id" value="<%=a.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <button type="submit">解封</button>
  </form>
  <a href="<%=request.getContextPath()%>/admin/knowledge" class="card" style="display:inline-block;padding:8px">返回列表</a>
<% } else { %>
  <div>文章不存在或已被删除</div>
<% } %>
</div>
</body>
</html>