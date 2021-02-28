{
  $.charts = {
		  line:{
			  option :  {
				   
				    tooltip : {
				        trigger: 'axis'
				    },
				    legend: {
				        data:[]
				    },
				    toolbox: {
				        show : false
				    },

				    xAxis : [
				        {
				            type : 'category',
				            boundaryGap : false,
				            data : []
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : []
				}
					                     
		  },
		  bar:{
			  option : {
					    
					    tooltip : {
					        trigger: 'axis'
					    },
					    legend: {
					        data:[]
					    },
					    toolbox: {
					        show : false
					    },
					    xAxis : [
					        {
					            type : 'category',
					            data : []
					        }
					    ],
					    yAxis : [
					        {
					            type : 'value'
					        }
					    ],
					    series : []
					}
					                    
		  },
		  pie:{
			  option : {
					    tooltip : {
					        trigger: 'item',
					        formatter: "{a} <br/>{b} : {c} ({d}%)"
					    },
					    legend: {
					        orient : 'vertical',
					        data:[]
					    },
					    toolbox: {
					        show : false
					    },
					    series : [ ]
					}
					                    
		  },
		  radar:{
			  option : {
			    
			      tooltip : {
			          trigger: 'axis'
			      },
			      legend: {
			          orient : 'vertical',
			          x : 'right',
			          y : 'bottom',
			          data:[]
			      },
			      toolbox: {
			          show : false
			      },
			      polar : [
			         {
			             indicator : [ ]
			          }
			      ],
			      series : [ ]
			  }
			                       
		  }
  };	
}