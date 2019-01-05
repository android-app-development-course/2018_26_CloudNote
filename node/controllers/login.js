const userModel = require('../lib/mysql.js');
const moment = require('moment');
const md5 = require('md5');

let fn_login = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    console.log(`signin with name: ${email}, password: ${pwd}`);

    let res = await userModel.findUserByEmail(email)
    console.log(res);
    try{
        user = res[0];
        if(user['password']  === md5(pwd))
        {
            ctx.body = {
                success: 1,
                nickname: res[0].nickname,
            }
        }
        else{
            ctx.body = {
                success: 0,
            }
        }
    } catch (err){
        ctx.body = {
            success: 0,
        }
    }

}

let fn_register = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    let name = ctx.request.body.name;
    console.log(`register with name: ${email}, password: ${pwd}`);

    let res = await userModel.findUserByEmail(email)
    if (res.length) {
        ctx.body = {
            success: 0,
        }
    } else {
        await userModel.insertUser([email, name, md5(pwd), moment().format('YYYY-MM-DD HH:mm:ss')])
            .then(res => {
                console.log('注册成功', res)
                //注册成功
                ctx.body = {
                    success: 1
                };
            })
    }
}

module.exports = {
    'POST /login': fn_login,
    'POST /register': fn_register,
};