import request from '@/utils/request'

export const getUsers = (params: any) =>
  request({
    url: '/users',
    method: 'get',
    params
  })

export const getUserInfo = (data: any): Promise<any> => {
  return new Promise((resolve, reject) => {
    resolve({
      data:
         {
           user: {
             id: 0,
             username: 'admin',
             password: 'any',
             name: 'Super Admin',
             avatar: 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif',
             introduction: 'I am a super administrator',
             email: 'admin@test.com',
             phone: '1234567890',
             roles: ['admin']
           }
         }
    })
  })
}
/*
  request({
    url: '/users/info',
    method: 'post',
    data
  })
*/

export const getUserByName = (username: string) =>
  request({
    url: `/users/${username}`,
    method: 'get'
  })

export const updateUser = (username: string, data: any) =>
  request({
    url: `/users/${username}`,
    method: 'put',
    data
  })

export const deleteUser = (username: string) =>
  request({
    url: `/users/${username}`,
    method: 'delete'
  })

export const login = (data: any): Promise<any> => {
  return new Promise((resolve, reject) => {
    resolve({ data: { accessToken: 'Success!' } })
  })
}
/*
request({
    url: '/users/login',
    method: 'post',
    data
  })
*/

export const logout = () =>
  request({
    url: '/users/logout',
    method: 'post'
  })

export const register = (data: any) =>
  request({
    url: '/users/register',
    method: 'post',
    data
  })
