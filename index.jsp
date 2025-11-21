<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<html>
<head><title>系统首页</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/static/style.css">
<script src="<%=request.getContextPath()%>/static/bubbles.js"></script>
<script>window.__ctx='<%=request.getContextPath()%>';</script>
<script src="<%=request.getContextPath()%>/static/nav-avatar.js"></script>
</head>
<body>
<div id="bg-slideshow"></div>
<div class="navbar">
  <div class="links">
    <a href="<%=request.getContextPath()%>/">用户主页</a>
    <a href="<%=request.getContextPath()%>/animals">流浪动物信息</a>
    <a href="<%=request.getContextPath()%>/user/animals" style="margin-left:auto">添加流浪动物</a>
    <a href="<%=request.getContextPath()%>/forum">论坛交流</a>
    <a href="<%=request.getContextPath()%>/knowledge">护养知识</a>
    <a href="<%=request.getContextPath()%>/user">个人中心</a>
    <a href="<%=request.getContextPath()%>/admin">管理</a>
  </div>
  <div class="right">
    <% if (session.getAttribute("userId") == null && session.getAttribute("adminId") == null) { %>
      <a href="<%=request.getContextPath()%>/login">登录</a>
      <a href="<%=request.getContextPath()%>/register">注册</a>
      <a href="<%=request.getContextPath()%>/login?role=admin">管理员登录</a>
    <% } else { %>
      <% String navAv = (String)session.getAttribute("avatarUrl"); String avatarSrc = null; if (navAv != null && !navAv.isEmpty()) { avatarSrc = (navAv.startsWith("http://")||navAv.startsWith("https://")||navAv.startsWith("/"))?navAv:(request.getContextPath()+"/"+navAv); } else { avatarSrc = request.getContextPath()+"/static/avatar.png"; } %>
      <div class="nav-avatar" data-user-url="<%=request.getContextPath()%>/user">
        <a href="#"><img src="<%=avatarSrc%>" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/></a>
        <div class="logout-pop"><a href="<%=request.getContextPath()%>/user">个人中心</a><a href="<%=request.getContextPath()%>/logout" style="margin-left:8px">退出登录</a></div>
      </div>
    <% } %>
  </div>
</div>
<div class="pageI">
  <h2>流浪动物收养系统</h2>
  <hr/>
  <% List<Map<String,Object>> topics = (List<Map<String,Object>>)request.getAttribute("topics"); %>
  <h3>论坛热门话题</h3>
  <div id="forum-slider" class="slider">
    <% java.text.SimpleDateFormat fmt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm"); %>
    <% if (topics != null) for (Map<String,Object> t: topics) { %>
      <a href="<%=request.getContextPath()%>/forum?id=<%=t.get("id")%>" class="card" style="display:inline-block;width:360px;margin:6px;vertical-align:top">
        <div style="display:flex;align-items:center;gap:10px">
          <% String tav = (String)t.get("avatar_url"); String tavSrc = (tav!=null && (tav.startsWith("http")||tav.startsWith("https")||tav.startsWith("/")))?tav:(tav==null?"":(request.getContextPath()+"/"+tav)); %>
          <img src="<%=tavSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
          <div>
            <div><b><%=t.get("username")%></b> · <span><%=t.get("title")%></span> · <small><%=fmt.format((java.util.Date)t.get("created_at"))%></small></div>
            <div style="color:#6e6e73">赞 <%=t.get("likes_count")%> · 评论 <%=t.get("comments_count")%></div>
          </div>
        </div>
      </a>
    <% } %>
  </div>
  <hr/>
  <% List<Map<String,Object>> articles = (List<Map<String,Object>>)request.getAttribute("articles"); %>
  <h3>护养知识</h3>
  <div id="knowledge-slider" class="slider">
    <% if (articles != null) for (Map<String,Object> a: articles) { %>
    <a href="<%=request.getContextPath()%>/knowledge?id=<%=a.get("id")%>" class="card" style="display:inline-block;width:360px;margin:6px;vertical-align:top">
      <div style="display:flex;align-items:center;gap:10px">
        <% String kav = (String)a.get("avatar_url"); String kavSrc = (kav!=null && (kav.startsWith("http")||kav.startsWith("https")||kav.startsWith("/")))?kav:(kav==null?"":(request.getContextPath()+"/"+kav)); %>
        <img src="<%=kavSrc%>" class="avatar avatar-sm" onerror="this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
        <div>
          <div><b><%=a.get("username")!=null?a.get("username"):a.get("author")%></b> · <span><%=a.get("title")%></span> · <small><%=fmt.format((java.util.Date)a.get("published_at"))%></small></div>
          <div style="color:#6e6e73">赞 <%=a.get("likes_count")%> · 评论 <%=a.get("comments_count")%></div>
        </div>
      </div>
    </a>
    <% } %>
  </div>
</div>
  <h3>待领养动物</h3>
  <div id="animals-grid" style="display:grid;grid-template-columns:repeat(3, 1fr);gap:12px">
  <%
    List<Map<String,Object>> animals = (List<Map<String,Object>>) request.getAttribute("animals");
    if (animals == null) {
      out.write("<div style='color:#ff4d88'>暂无数据或加载失败</div>");
    } else if (animals.isEmpty()) {
      out.write("<div>暂无待领养动物</div>");
    } else {
      for (Map<String,Object> a : animals) {
  %>
    <div class="card" style="height:300px;">
      <div>
        <% String p = (String)a.get("photo_url"); String src = (p!=null && (p.startsWith("http://")||p.startsWith("https://")||p.startsWith("/"))) ? p : (p==null?"":(request.getContextPath()+"/"+p)); %>
        <img src="<%=src%>" alt="photo" style="max-width:100%;max-height:160px" onerror="this.onerror=null;this.src='<%=request.getContextPath()%>/static/avatar.png'"/>
      </div>
      <div><b><%=a.get("name")%></b> / <%=a.get("species")%> / <%=a.get("breed")%></div>
      <div>年龄：<%=a.get("age")%> 健康：<%=a.get("health_status")%></div>
      <div>
        <a href="<%=request.getContextPath()%>/animals?id=<%=a.get("id")%>">详情</a>
        <% if (session.getAttribute("userId") != null) { %>
          <a href="<%=request.getContextPath()%>/animals?id=<%=a.get("id")%>&apply=1">领养</a>
        <% } else { %>
          <a href="<%=request.getContextPath()%>/login">领养</a>
        <% } %>
      </div>
    </div>
  <%
      }
    }
  %>
  </div>
  <hr/>
<%
  java.io.File picDir = new java.io.File(application.getRealPath("/animalsPicture"));
  String[] imgs = picDir != null && picDir.exists() ? picDir.list((d,name)->name.matches("(?i).+\\.(png|jpg|jpeg|gif)$")) : new String[0];
%>
 
<script>
  const ctx = '<%=request.getContextPath()%>';
  const imgs = [<% for (int i = 0; i < imgs.length; i++) { String name = imgs[i] == null ? "" : imgs[i].replace("'", "\\'"); out.write("'" + name + "'"); if (i < imgs.length - 1) out.write(","); } %>];
  let idx = 0;
  function nextBg(){
    if(!imgs || imgs.length===0) return;
    const el = document.getElementById('bg-slideshow');
    el.style.backgroundImage = `url('${ctx}/animalsPicture/${imgs[idx]}')`;
    idx = (idx + 1) % imgs.length;
  }
  nextBg();
  setInterval(nextBg, 5000);
  function autoScroll(id){
    const el=document.getElementById(id); if(!el) return;
    const children=Array.from(el.children);
    let total=children.reduce((s,c)=>{ const st=getComputedStyle(c); return s + c.offsetWidth + parseInt(st.marginLeft||'0') + parseInt(st.marginRight||'0'); },0);
    while(total < el.clientWidth*2 && children.length>0){
      children.forEach(ch=>{ const clone=ch.cloneNode(true); el.appendChild(clone); });
      total = Array.from(el.children).reduce((s,c)=>{ const st=getComputedStyle(c); return s + c.offsetWidth + parseInt(st.marginLeft||'0') + parseInt(st.marginRight||'0'); },0);
    }
    let timer;
    const step=()=>{
      el.scrollLeft+=1;
      const first=el.firstElementChild;
      if(first){ const st=getComputedStyle(first); const w=first.offsetWidth+parseInt(st.marginLeft||'0')+parseInt(st.marginRight||'0'); if(el.scrollLeft>=w){ el.appendChild(first); el.scrollLeft-=w; } }
    };
    timer=setInterval(step, 24);
    el.addEventListener('mouseenter',()=>{ clearInterval(timer); });
    el.addEventListener('mouseleave',()=>{ timer=setInterval(step, 24); });
  }
  autoScroll('forum-slider');
  autoScroll('knowledge-slider');
</script>
</body>
</html>
