<template>
  <div class="animate__animated animate__fadeIn">
    <el-card>
      <!--搜索部分-->
      <div class="search-container">
        <div class="search-box">
          <el-input prefix-icon="el-icon-search" v-model="searchCondition.nickname" placeholder="请输用户昵称..."
                    size="small" clearable @clear="initData" style="margin-right: 10px;width: 25%;"/>
          <el-input prefix-icon="el-icon-search" v-model="searchCondition.ipSource" placeholder="请输入用户地区..."
                    size="small" clearable @clear="initData" style="margin-right: 10px;width: 25%;"/>
          <el-select size="small" v-model="searchCondition.status" placeholder="登陆状态"
                     clearable @clear="initData" style="margin-left: 10px;width: 15%;">
            <el-option
                v-for="item in status"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </div>
        <div class="time-box">
          <el-date-picker
              style="margin-right: 10px"
              @change="change"
              v-model="time"
              type="daterange"
              size="small"
              unlink-panels
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              :picker-options="pickerOptions">
          </el-date-picker>
          <el-button icon="el-icon-search" type="primary" size="small" @click="initData">搜索</el-button>
        </div>
      </div>
      <!--表格部分-->
      <el-table
          ref="multipleTable"
          v-loading="loading"
          :data="logList"
          tooltip-effect="dark"
          style="width: 100%"
          @selection-change="selectionChange">
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            align="center"
            prop="username"
            label="用户名"
            min-width="100">
        </el-table-column>
        <el-table-column
            align="center"
            prop="nickname"
            label="用户名称"
            min-width="150">
        </el-table-column>
        <el-table-column
            align="center"
            prop="ipAddress"
            label="登陆ip地址"
            width="120">
        </el-table-column>
        <el-table-column
            align="center"
            prop="ipSource"
            label="登陆ip所在地"
            width="140">
        </el-table-column>
        <el-table-column
            align="center"
            prop="brower"
            label="浏览器"
            width="90">
        </el-table-column>
        <el-table-column
            align="center"
            prop="os"
            label="操作系统"
            width="90">
        </el-table-column>
        <el-table-column
            align="center"
            label="状态"
            width="80">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.status === 1">成功</el-tag>
            <el-tag v-else type="danger">失败</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            align="center"
            prop="remark"
            label="操作信息"
            min-width="120">
        </el-table-column>
        <el-table-column
            align="center"
            prop="gmtCreate"
            label="登陆时间"
            width="180">
          <template slot-scope="scope">
            <i class="el-icon-time"></i>
            {{ scope.row.gmtCreate }}
          </template>
        </el-table-column>
      </el-table>
      <!--分页部分-->
      <el-pagination
          class="page"
          @size-change="sizeChange"
          @current-change="currentChange"
          :current-page="searchCondition.current"
          :page-sizes="[8, 16, 32]"
          :page-size="8"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
import {formatTimeToYMD} from "../../../utils/timeUtil";
import {listLoginLog} from "../../../api/log/loginLog/LoginLog";
import {resultCheck} from "../../../utils/result";

export default {
  data() {
    return {
      logList: [],
      loading: false,
      deleteList: [],
      total: 0,
      time: [],
      searchCondition: {
        current: 1,
        size: 8,
        nickname: "",
        ipSource: "",
        status: null,
        beginTime: "",
        endTime: ""
      },
      status: [
        {value: 1, label: "成功"},
        {value: 0, label: "失败"}
      ],
      pickerOptions: {
        shortcuts: [{
          text: '最近一周',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近一个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
            picker.$emit('pick', [start, end]);
          }
        }, {
          text: '最近三个月',
          onClick(picker) {
            const end = new Date();
            const start = new Date();
            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
            picker.$emit('pick', [start, end]);
          }
        }]
      },
    }
  },
  mounted() {
    this.initData();
  },
  methods: {
    //=======================  查询操作  ===========================
    initData() {
      this.loading = true;
      listLoginLog(this.searchCondition).then(res => {
        var data = resultCheck(res, false);
        this.logList = data.data;
        this.total = data.total;
        this.loading = false;
      });
    },
    sizeChange(val) {
      this.searchCondition.size = val;
      this.initData();
    },
    currentChange(val) {
      this.searchCondition.current = val;
      this.initData();
    },
    // 时间变换
    change() {
      this.searchCondition.beginTime = formatTimeToYMD(this.time[0]);
      this.searchCondition.endTime = formatTimeToYMD(this.time[1]);
    },
    // 批量删除
    selectionChange(val) {
      this.deleteList = val;
    },

  }
}
</script>

<style scoped>
.search-container {
  display: flex;
  flex-direction: column;
}

.search-box {
  display: flex;
  justify-content: left;
}

.time-box {
  display: flex;
  justify-content: left;
  margin-top: 10px;
}

.page{
  display: flex;
  justify-content: right;
  margin-top: 10px;
}
</style>