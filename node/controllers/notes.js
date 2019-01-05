const userModel = require('../lib/mysql.js');
const md5 = require("md5");
const moment = require("moment");

let verify = async (email, pwd) => {
    res = await userModel.findUserByEmail(email);
    if (res.length == 0) {
        return {
            status: false
        };
    }
    if (res[0]['password'] !== md5(pwd)) {
        return {
            status: false
        };
    }
    return {
        status: true,
        uid: res[0]['uid']
    };
}

let fn_insetNote = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    let title = ctx.request.body.title;
    let content = ctx.request.body.content;

    let r = await verify(email, pwd);

    if (r.status) {
        let res = await userModel.insertNote([r.uid, title, content, moment().format('YYYY-MM-DD HH:mm:ss')])
        console.log(res);
        ctx.body = {
            success: 1,
            nid: res['insertId'],
        }
    } else {
        ctx.body = {
            success: 0,
        }
    }

}

let fn_updateNote = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    let nid = ctx.request.body.nid;
    let title = ctx.request.body.title;
    let content = ctx.request.body.content;

    let r = await verify(email, pwd);
    if (r.status) {
        let res = await userModel.updateNote([title, content, nid]);
        ctx.body = {
            success: 1,
        }
    } else {
        ctx.body = {
            success: 0,
        }
    }
}

let fn_deleteNote = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    let nid = ctx.request.body.nid;

    let r = await verify(email, pwd);
    if (r.status) {
        let res = await userModel.deleteNote([nid]);
        ctx.body = {
            success: 1,
        }
    } else {
        ctx.body = {
            success: 0,
        }
    }
}

let fn_deleteAllNote = async(ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;

    let r = await verify(email, pwd);
    if (r.status){
        let res = await userModel.deleteAllNote(r.uid);
        ctx.body = {
            success : 1
        }

    }
    else{
        ctx.body = {
            success : 0
        }
    }
}

let fn_getNote = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;
    let nid = ctx.request.body.nid;

    let r = await verify(email, pwd);
    if (r.status) {
        let res = await userModel.getNoteByNid([nid]);
        ctx.body = {
            success: 1,
            data: res[0],
        }
    } else {
        ctx.body = {
            success: 0,
        }
    }

}

let fn_getNids = async (ctx, next) => {
    let email = ctx.request.body.email;
    let pwd = ctx.request.body.password;

    let r = await verify(email, pwd);
    if (r.status) {
        let res = await userModel.getNidsByUid([r.uid]);
        let ar = [];
        console.log(res);
        for(let i = 0; i < res.length; ++i){
            ar.push(res[i].nid);
        }
        ctx.body = {
            success: 1,
            data: ar,
        }
    } else {
        ctx.body = {
            success: 0,
        }
    }
}


module.exports = {
    'POST /addnote': fn_insetNote,
    'POST /updatenote': fn_updateNote,
    'POST /deletenote': fn_deleteNote,
    'POST /deleteallnote': fn_deleteAllNote,
    'POST /getnote': fn_getNote,
    'POST /getnids': fn_getNids,
};