### 登录
1. 请求地址
    >`POST  http://118.24.233.201:3000/register`
2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|
>|nickname|string|成功时才有，用户名|

### 注册
1. 请求地址
    >`POST  http://118.24.233.201:3000/login`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|
>|name|string|用户名|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|

### 添加笔记
1. 请求地址
    >`POST  http://118.24.233.201:3000/addnote`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|
>|title|string|标题|
>|content|string|内容|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|
>|nid|int|笔记的唯一标识符|

### 更新笔记
1. 请求地址
    >`POST  http://118.24.233.201:3000/updatenote`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|
>|title|string|标题|
>|content|string|内容|
>|nid|string|笔记的唯一标识符|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|

### 删除笔记
1. 请求地址
    >`POST  http://118.24.233.201:3000/deletenote`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|
>|nid|string|笔记的唯一标识符|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|

### 获取笔记内容
1. 请求地址
    >`POST  http://118.24.233.201:3000/getnote`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|
>|nid|string|笔记的唯一标识符|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|
>|data|object|笔记的内容，nid,uid,title,content,submission_date（创建时间）|

### 获取NID
1. 请求地址
    >`POST  http://118.24.233.201:3000/getnids`

2. 请求参数  

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|email|string|电子邮件|
>|password|string|密码|

3. 返回参数  
返回的 JSON 数据包

>| 属性 | 类型 | 说明 |
>| --- | :--: | -----: |
>|success|int|1代表成功，0代表失败|
>|data|array|NID数组