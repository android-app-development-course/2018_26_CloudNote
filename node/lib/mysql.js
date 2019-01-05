let mysql = require('mysql');
let config = require('../config/default.js')

var pool = mysql.createPool({
    host: config.database.host,
    user: config.database.user,
    password: config.database.password,
    database: config.database.database,
});

let query = function (sql, values) {

    return new Promise((resolve, reject) => {
        pool.getConnection(function (err, connection) {
            if (err) {
                reject(err)
            } else {
                connection.query(sql, values, (err, rows) => {

                    if (err) {
                        reject(err)
                    } else {
                        resolve(rows)
                    }
                    connection.release()
                })
            }
        })
    })

}

// 注册用户
let insertUser = function (value) {
    let _sql = "insert into user set email =?,nickname=?,password=?,submission_date=?;"
    return query(_sql, value);
}

let findUserByEmail = function (email) {
    let _sql = `select * from user where email="${email}";`
    return query(_sql);
}

let insertNote = function (value) {
    let _sql = "insert into notes set uid =?, title=?, content=?,submission_date=?;";
    return query(_sql, value);
}

let updateNote = function (value) {
    let _sql = "update notes set title=?, content=? where nid=?"
    return query(_sql, value);
}

let deleteNote = function(nid){
    let _sql = `delete from notes where nid="${nid}";`
    return query( _sql );
}

let getNoteByNid = function(nid){
    let _sql = `select * from notes where nid="${nid}";`
    return query(_sql);
}

let getNidsByUid = function(uid){
    let _sql = `select nid from notes where uid="${uid}";`
    return query(_sql);
}

let deleteAllNote = function(uid){
    let _sql = `delete from notes where uid="${uid}";`;
    return query(_sql)
}

module.exports ={
    query,
    insertUser,
    findUserByEmail,
    insertNote,
    updateNote,
    deleteNote,
    getNoteByNid,
    getNidsByUid,
    deleteAllNote,
};