<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>测试</title>
</head>

<body>
<#list users as user>
    <h1>你好${user.name}</h1>
</#list>
<#if test.test2.pre??>
    <h1>${test.test2.pre?string('true','false')}</h1>
    <h1>${test.number}</h1>
<#else >
    <h1>false</h1>
</#if>
</body>
</html>