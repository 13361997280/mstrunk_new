webpackJsonp([17],{455:function(e,t,r){var o=r(0)(r(648),r(720),null,null,null);e.exports=o.exports},648:function(e,t,r){"use strict";Object.defineProperty(t,"__esModule",{value:!0});var o=r(25),n=function(e){return e&&e.__esModule?e:{default:e}}(o),l=r(53);t.default={data:function(){return{role:""}},computed:(0,n.default)({},(0,l.mapGetters)(["roles"])),watch:{role:function(e){this.$store.commit("SET_ROLES",[e])}}}},720:function(e,t){e.exports={render:function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"app-container"},[r("div",{staticStyle:{"margin-bottom":"15px"}},[e._v("你的权限： "+e._s(e.roles))]),e._v("\n   切换权限：\n    "),r("el-radio-group",{model:{value:e.role,callback:function(t){e.role=t},expression:"role"}},[r("el-radio-button",{attrs:{label:"editor"}}),e._v(" "),r("el-radio-button",{attrs:{label:"developer"}})],1)],1)},staticRenderFns:[]}}});
//# sourceMappingURL=17.40018300943704f4dac8.js.map