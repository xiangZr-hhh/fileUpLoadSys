package com.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sys.constans.ResponseResult;
import com.sys.entity.*;
import com.sys.mapper.*;
import com.sys.service.GroupOrderService;
import com.sys.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * (GroupOrder)表服务实现类
 *
 * @author zrx
 * @since 2023-12-23 11:48:34
 */
@Service("groupOrderService")
public class GroupOrderServiceImpl extends ServiceImpl<GroupOrderMapper, GroupOrder> implements GroupOrderService {

    @Autowired
    private GroupOrderMapper groupOrderMapper;
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StandardMapper standardMapper;
    @Autowired
    private FileMapper fileMapper;


    //    获取所有分组数据
    @Override
    public ResponseResult getAll() {

//        获取所有分组排序
        List<GroupOrder> groupOrders = groupOrderMapper.selectList(null);
//        封装结果类
        List<GroupListVo> groupListVos = new ArrayList<>();


//        获取结果类数据
        for (GroupOrder groupOrder : groupOrders) {

//            先获取产品规格
            Standard standard = standardMapper.selectById(groupOrder.getStandardId());
//            封装产品规格vo类
            StandardListVo standardListVo = new StandardListVo(standard.getId(), standard.getStandardName());
//            检查结果类中是否含有 该条排序信息中的产品规格id
            boolean judgeProduct = false;

            outerLoop:
            for (GroupListVo groupListVo : groupListVos) {
                innerLoop:
                for (ProductListVo productListVo : groupListVo.getProductList()) {
//                    如果id为-1，代表无数据，则自动创建新对象
                    if (groupOrder.getStandardId() == -1) {
                        judgeProduct = false;
                        break outerLoop;
                    }
//                    如果包含
                    if (productListVo.getId() == groupOrder.getProductId()) {
//                       先将判断值改为正确,既含有
                        judgeProduct = true;
//                        再在原有的对象上修改属性，不在创建新对象
                        productListVo.getStandardList().add(standardListVo);
                        break outerLoop;
                    }

                }
            }
//           如果不包含
            if (!judgeProduct) {
//            不包含，则创建一个新的Product对象
                Product product = productMapper.selectById(groupOrder.getProductId());
                List<StandardListVo> standardListVos = new ArrayList<>();
                standardListVos.add(standardListVo);
                ProductListVo productListVo = new ProductListVo(product.getId(), product.getProductName(), standardListVos);

                //          再检查结果类里是否含有对应的项目号类projectId
                //                  判断值
                boolean judgeProject = false;
                for (GroupListVo groupListVo : groupListVos) {
//                    如果id为-1，代表无数据，则自动创建新对象
                    if (groupOrder.getProductId() == -1) {
                        break;
                    }
                    //如果包含对应project项目号对象
                    if (groupListVo.getId() == groupOrder.getProjectId()) {
//                        再在原有的对象上修改属性，不在创建新对象
                        groupListVo.getProductList().add(productListVo);
                        judgeProject = true;
                        break;
                    }
                }
//                如果没有该分组
                if (!judgeProject) {
//                    创建一个新对象
                    Project project = projectMapper.selectById(groupOrder.getProjectId());
                    List<ProductListVo> productListVos = new ArrayList<>();
                    productListVos.add(productListVo);
                    GroupListVo groupListVo = new GroupListVo(project.getId(), project.getProjectName(), productListVos);
                    groupListVos.add(groupListVo);
                }

            }

        }

        return ResponseResult.okResult(groupListVos);

    }


    @Override
    public ResponseResult deleteGroup(GroupVo groupVo) {

        int projectId = groupVo.getProjectId();
        int productId = groupVo.getProductId();
        int standardId = groupVo.getStandardId();

//        查询分组排序
        LambdaQueryWrapper<GroupOrder> gWrapper = new LambdaQueryWrapper<>();
        gWrapper.eq(GroupOrder::getProjectId, projectId)
                .eq(GroupOrder::getProductId, productId)
                .eq(GroupOrder::getStandardId, standardId);
        GroupOrder groupOrder = groupOrderMapper.selectOne(gWrapper);

//        根据分组排序Id删除对应文件
        LambdaQueryWrapper<Myfile> myFileWrapper = new LambdaQueryWrapper<>();
        myFileWrapper.eq(Myfile::getGroupId, groupOrder.getId());
        fileMapper.delete(myFileWrapper);


//        如果产品号和产品规格都为暂无数据，说明要删除项目号
        if (productId == -1 && standardId == -1) {
            projectMapper.deleteById(projectId);
        }
//        如果仅为产品规格为暂无数据，说明要删除产品号
        if (productId != -1 && standardId == -1) {
            deleteJudge(projectId, projectId, 1);
            productMapper.deleteById(productId);
        }
//        如果都有数据，说明删除产品规格即可
        if (standardId != -1) {
            deleteJudge(productId, projectId, 2);
            standardMapper.deleteById(standardId);
        }
        //      删除四个表的id
        groupOrderMapper.deleteById(groupOrder.getId());

        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult addGroup(AddGroupRequestVo addGroupResponseVo) {

        int isCreateProject = addGroupResponseVo.getIsCreateProject();
        int isCreateProduct = addGroupResponseVo.getIsCreateProduct();
        String projectName = addGroupResponseVo.getProjectName();
        String productName = addGroupResponseVo.getProductName();
        String standardName = addGroupResponseVo.getStandardName();
        int projectId = addGroupResponseVo.getProjectId();
        int productId = addGroupResponseVo.getProductId();

//        如果创建一个全新的分组
        if (isCreateProject == 1 && isCreateProduct == 1 && !standardName.equals("")) {
            Project project = new Project(projectName);
            Product product = new Product(productName);
            Standard standard = new Standard(standardName);
            projectMapper.insert(project);
            insertJudge(project.getId(), project.getId(), 1);
            productMapper.insert(product);
            insertJudge(product.getId(), project.getId(), 2);
            standardMapper.insert(standard);
            GroupOrder groupOrder = new GroupOrder(project.getId(), product.getId(), standard.getId());
            groupOrderMapper.insert(groupOrder);
        }
//        创建一个全新项目号、产品号、不创建产品规格
        if (isCreateProject == 1 && isCreateProduct == 1 && standardName.equals("") && !productName.equals("")) {
            Project project = new Project(projectName);
            Product product = new Product(productName);
            projectMapper.insert(project);
            insertJudge(project.getId(), project.getId(), 1);
            productMapper.insert(product);
            insertJudge(product.getId(), project.getId(), 2);
            GroupOrder groupOrder = new GroupOrder(project.getId(), product.getId(), -1);
            groupOrderMapper.insert(groupOrder);
        }
//        如果是创建一个新产品号，不创建产品规格，选择已有项目号
        if (isCreateProject == 2 && isCreateProduct == 1 && standardName.equals("")) {
            insertJudge(projectId, projectId, 1);
            Product product = new Product(productName);
            productMapper.insert(product);
            insertJudge(product.getId(), projectId, 2);
            GroupOrder groupOrder = new GroupOrder(projectId, product.getId(), -1);
            groupOrderMapper.insert(groupOrder);
        }
//        如果是创建一个新产品号,新产品规格，选择已有项目号
        if (isCreateProject == 2 && isCreateProduct == 1 && !standardName.equals("")) {
            Product product = new Product(productName);
            Standard standard = new Standard(standardName);
            productMapper.insert(product);
            insertJudge(product.getId(), projectId, 2);
            standardMapper.insert(standard);
            GroupOrder groupOrder = new GroupOrder(projectId, product.getId(), standard.getId());
            groupOrderMapper.insert(groupOrder);
        }
//        如果是创建一个产品规格，选择已有产品号，项目号
        if (isCreateProject == 2 && isCreateProduct == 2 && !standardName.equals("")) {
            insertJudge(productId, projectId, 2);
            Standard standard = new Standard(standardName);
            standardMapper.insert(standard);
            GroupOrder groupOrder = new GroupOrder(projectId, productId, standard.getId());
            groupOrderMapper.insert(groupOrder);
        }
//        如果是创建一个全新的项目号
        if (isCreateProject == 1 && isCreateProduct == 1 && productName.equals("") && standardName.equals("")) {
            Project project = new Project(projectName);
            projectMapper.insert(project);
            GroupOrder groupOrder = new GroupOrder(project.getId(), -1, -1);
            groupOrderMapper.insert(groupOrder);
        }

        return ResponseResult.okResult();
    }


    //    添加分组时对-1暂无数据的处理
    public void insertJudge(int id, int projectId, int method) {
//        1为项目号
        if (method == 1) {
            LambdaQueryWrapper<GroupOrder> groupOrderWrapper = new LambdaQueryWrapper<>();
            groupOrderWrapper.eq(GroupOrder::getProjectId, id);
            List<GroupOrder> groupOrders = groupOrderMapper.selectList(groupOrderWrapper);
            for (GroupOrder groupOrder : groupOrders) {
                if (groupOrder.getProductId() == -1) {
                    groupOrderMapper.deleteById(groupOrder.getId());
                }
            }
        }
//        2为产品号
        if (method == 2) {
            LambdaQueryWrapper<GroupOrder> groupOrderWrapper = new LambdaQueryWrapper<>();
            groupOrderWrapper.eq(GroupOrder::getProjectId, projectId)
                    .eq(GroupOrder::getProductId, id);
            List<GroupOrder> groupOrders = groupOrderMapper.selectList(groupOrderWrapper);
            for (GroupOrder groupOrder : groupOrders) {
                if (groupOrder.getStandardId() == -1) {
                    groupOrderMapper.deleteById(groupOrder.getId());
                }
            }
        }
    }

    //    删除时对-1数据的处理
    public void deleteJudge(int id, int projectId, int method) {
//        1为项目号
        if (method == 1) {
            LambdaQueryWrapper<GroupOrder> groupOrderWrapper = new LambdaQueryWrapper<>();
            groupOrderWrapper.eq(GroupOrder::getProjectId, id);
            List<GroupOrder> groupOrders = groupOrderMapper.selectList(groupOrderWrapper);
//            如果只剩下了一个实际数据,添加一个暂无数据的标签
            if (groupOrders.size() == 1 && groupOrders.get(0).getProductId() != -1) {
                GroupOrder groupOrder = new GroupOrder(id, -1, -1);
                groupOrderMapper.insert(groupOrder);
            }
        }
//        2为产品号
        if (method == 2) {
            LambdaQueryWrapper<GroupOrder> groupOrderWrapper = new LambdaQueryWrapper<>();
            groupOrderWrapper.eq(GroupOrder::getProjectId, projectId).eq(GroupOrder::getProductId, id);
            List<GroupOrder> groupOrders = groupOrderMapper.selectList(groupOrderWrapper);
//            如果只剩下了一个实际数据,添加一个暂无数据的标签
            if (groupOrders.size() == 1 && groupOrders.get(0).getStandardId() != -1) {
                GroupOrder groupOrder = new GroupOrder(projectId, id, -1);
                groupOrderMapper.insert(groupOrder);
            }
        }
    }


}
