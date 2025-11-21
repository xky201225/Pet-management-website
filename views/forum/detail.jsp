<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>话题详情</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/forum">论坛</a>
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
<% Map<String,Object> t = (Map<String,Object>)request.getAttribute("topic"); List<Map<String,Object>> comments = (List<Map<String,Object>>)request.getAttribute("comments"); java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
<% if (t != null) { %>
  <h3><%=t.get("title")%></h3>
  <div style="display:flex;align-items:center;gap:8px;color:#6e6e73">
    <% String av = (String)t.get("avatar_url"); String avSrc = (av!=null && (av.startsWith("http")||av.startsWith("/")))?av:(av==null?"":(request.getContextPath()+"/"+av)); %>
    <a href="<%=request.getContextPath()%>/user/profile?id=<%=t.get("user_id")%>"><img src="<%=avSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
    <span><%=t.get("username")%></span>
    <small><%=fmt.format((java.util.Date)t.get("created_at"))%></small>
  </div>
  <div style="margin-top:12px;white-space:pre-wrap"><%=t.get("content")%></div>
  <form method="post" action="<%=request.getContextPath()%>/forum" style="display:inline-block;margin-right:8px">
    <input type="hidden" name="action" value="like"/>
    <input type="hidden" name="topicId" value="<%=t.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <button type="submit">点赞(<%=t.get("likes_count")%>)</button>
  </form>
  <hr/>
  <h4>评论</h4>
  <form method="post" action="<%=request.getContextPath()%>/forum">
    <input type="hidden" name="action" value="comment"/>
    <input type="hidden" name="topicId" value="<%=t.get("id")%>"/>
    <input type="hidden" name="returnDetail" value="1"/>
    <div class="form-row"><label>内容</label><input name="content" required/></div>
    <div class="form-actions"><button type="submit">发表评论</button></div>
  </form>
  <% if (comments != null) for (Map<String,Object> c: comments) { %>
    <div style="padding:6px 0;border-top:1px dashed #eee;display:flex;gap:8px;align-items:center">
      <% String cav = (String)c.get("avatar_url"); String cavSrc = (cav!=null && (cav.startsWith("http")||cav.startsWith("/")))?cav:(cav==null?"":(request.getContextPath()+"/"+cav)); %>
      <a href="<%=request.getContextPath()%>/user/profile?id=<%=c.get("user_id")%>"><img src="<%=cavSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
      <div><b><%=c.get("username")%></b> · <small><%=fmt.format((java.util.Date)c.get("created_at"))%></small><div><%=c.get("content")%></div></div>
    </div>
  <% } %>
<% } else { %>
  <div>话题不存在或已被隐藏</div>
<% } %>
</div>
</body>
</html>
