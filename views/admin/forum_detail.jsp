<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>话题详情（管理员）</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/admin">管理员主页</a>
    <a href="<%=request.getContextPath()%>/admin/forum">论坛管理</a>
  </div>
  <div class="right"><a href="<%=request.getContextPath()%>/logout">退出</a></div>
</div>
<div class="page">
<% Map<String,Object> t = (Map<String,Object>)request.getAttribute("topic"); java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<% if (t != null) { %>
  <h3><%=t.get("title")%></h3>
  <div style="display:flex;align-items:center;gap:8px;color:#6e6e73">
    <% String av = (String)t.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
    <img src="<%=avSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
    <span><%=t.get("username")%></span>
    <small><%=fmt.format((java.util.Date)t.get("created_at"))%></small>
  </div>
  <div style="margin-top:12px;white-space:pre-wrap"><%=t.get("content")%></div>
  <div style="margin-top:8px;color:#6e6e73">赞 <%=t.get("likes_count")%> · 评论 <%=t.get("comments_count")%> · 状态：<span class="<%= "visible".equals(t.get("status"))?"status-ok":"status-bad" %>"><%=t.get("status")%></span></div>
  <hr/>
  <form method="post" action="<%=request.getContextPath()%>/admin/forum" style="display:inline-block;margin-right:8px">
    <input type="hidden" name="action" value="hideTopic"/>
    <input type="hidden" name="id" value="<%=t.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <button type="submit">屏蔽</button>
  </form>
  <form method="post" action="<%=request.getContextPath()%>/admin/forum" style="display:inline-block;margin-right:8px">
    <input type="hidden" name="action" value="showTopic"/>
    <input type="hidden" name="id" value="<%=t.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <button type="submit">解封</button>
  </form>
  <a href="<%=request.getContextPath()%>/admin/forum" class="card" style="display:inline-block;padding:8px">返回列表</a>
<% } else { %>
  <div>话题不存在或已被隐藏</div>
<% } %>
</div>
</body>
</html>