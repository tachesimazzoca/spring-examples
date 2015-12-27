<#import "/_layouts/default.ftl" as layout>
<#import "/_macros/helpers.ftl" as helpers>
<#assign htmlEscape=true in helpers>

<@layout.defaultLayout "Dashboard">

<div class="panel panel-default">
  <div class="panel-heading">
    <h2 class="panel-title">Profile</h2>
  </div>
  <div class="panel-body">
    <dl class="dl-horizontal">
      <dt>Email</dt>
      <dd></dd>
      <dt>Nickname</dt>
      <dd></dd>
    </dl>
    <div class="text-right">
      <a href="${config.url.basedir}/profile/edit" class="btn btn-default">Edit</a>
    </div>
  </div>
</div>

<div>
<ul class="list-group">
   <li class="list-group-item"><a href="${config.url.basedir}/dashboard/questions">Questions</a></li>
   <li class="list-group-item"><a href="${config.url.basedir}/dashboard/answers">Answers</a></li>
</ul>
</div>

</@layout.defaultLayout>
