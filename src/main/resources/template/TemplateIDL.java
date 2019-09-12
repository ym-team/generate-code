package upload;


import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;


import com.evergrande.generator.ui.annotation.ApiImplicitParams;
import com.evergrande.generator.ui.annotation.ApiImplicitParam;
import com.evergrande.generator.ui.annotation.ApiOperation;
import com.evergrande.generator.ui.annotation.ApiVersion;
import com.evergrande.generator.ui.annotation.PageInfo;
import com.evergrande.generator.ui.annotation.PathVariable;
import com.evergrande.generator.ui.annotation.RequestBody;
import com.evergrande.generator.ui.annotation.RequestMapping;
import com.evergrande.generator.ui.annotation.RequestMethod;
import com.evergrande.generator.ui.annotation.controller;
import com.evergrande.generator.ui.annotation.default_value;
import com.evergrande.generator.ui.annotation.doc;
import com.evergrande.generator.ui.annotation.namespace;
import com.evergrande.generator.ui.annotation.required;
import com.evergrande.generator.ui.annotation.struct;
import com.evergrande.generator.ui.annotation.tag;

@namespace(name = "com.evergrande.orderserver")
public class TemplateIDL {
	
	@doc(name = "保存实体的描述信息")
	@struct(name= "GroupVoS")//类名
	class GroupVoS{

		@doc(name = "标签编码")
		String labelCodeS;
		
		@doc(name = "标签描述")
		String labelNameS;
		
		
		@doc(name = "有效时间，时间有效期到期后延后一周凌晨整点标签状态变为无效")
		Long expDateS;
		

	}
	
	@doc(name = "查询实体的描述信息")
	@struct(name= "GroupVoQ")//类名
	class GroupVoQ{

		
		@doc(name = "总条数")
		Long totalQ;
		
		@doc(name = "第几分页")
		Long offsetQ;
		
		@doc(name = "当前分页显示条数")
		Long countQ;
		
	}
	
	@doc(name = "更新实体的描述信息")
	@struct(name= "GroupVoU")//类名
	class GroupVoU {

		@doc(name = "判断是否有对应标签数据")
		Boolean stateU;
	}
	
	
	@doc(name = "删除实体的描述信息")
	@struct(name= "GroupVoD")//类名
	class GroupVoD {

		
		@doc(name = "判断是否有对应标签数据")
		Boolean stateD;
	}

	@doc(name = "controller的描述信息")
	interface RealTimeUserController {
		
	    @ApiOperation(value = "保存采购商信息接口", notes = "保存采购商信息接口")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "GroupVoS", value = "vos", required = true, defaultValue = "null", dataType = "GroupVoS", paramType = "GroupVoS")
	    })
	    @RequestMapping(value = "/saveGroupVoS", method = RequestMethod.GET)
	    @ApiVersion("1.0.0")
		String save(@RequestBody GroupVoS vos)throws Exception;
	
	    
		
	    @ApiOperation(value = "获取采购商信息接口", notes = "根据ipurchaserId获取采购商PurchaserVo对应的Url信息")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "purchaserId", value = "采购商ID", required = true, defaultValue = "0", dataType = "long", paramType = "path")
	    })
	    @RequestMapping(value = "/purchaserbypurchaserid/{purchaserId}", method = RequestMethod.GET)
	    @ApiVersion("1.0.0")
		String queryDetail(@PathVariable("purchaserId") long purchaserId)throws Exception;
	    
	    
	    
	    @ApiOperation(value = "获取采购商信息列表接口", notes = "根据GroupVoQ获取采购商GroupVoQ对应的Url信息")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "GroupVoQ", value = "voq实体", required = true, defaultValue = "null", dataType = "GroupVoQ", paramType = "GroupVoQ")
	    })
	    @RequestMapping(value = "/queryList", method = RequestMethod.GET)
	    @ApiVersion("1.0.0")
		String queryList(@RequestBody GroupVoQ voq)throws Exception;
	    
	    
	    @ApiOperation(value = "分页获取采购商信息接口", notes = "根据GroupVoQ和Page获取采购商GroupVoQ对应的Url信息")
	    @ApiImplicitParams({
	            @ApiImplicitParam(name = "GroupVoQ", value = "group实体", required = true, defaultValue = "null", dataType = "GroupVoQ", paramType = "GroupVoQ"),
	            @ApiImplicitParam(name = "Page", value = "page实体", required = true, defaultValue = "null", dataType = "Page", paramType = "Page")
	    })
	    @RequestMapping(value = "/queryListByPage", method = RequestMethod.GET)
	    @ApiVersion("1.0.0")
		String queryListByPage(@RequestBody GroupVoQ voq,PageInfo page)throws Exception;
	    
	}
	
	
}

