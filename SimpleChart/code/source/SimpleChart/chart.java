package SimpleChart;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2006-07-28 17:15:06 JST
// -----( ON-HOST: panw4

import com.wm.data.*;
import com.wm.util.Values;
import com.wm.app.b2b.server.Service;
import com.wm.app.b2b.server.ServiceException;
// --- <<IS-START-IMPORTS>> ---
import java.util.Hashtable;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;
import java.text.*;
import java.io.*;
import javax.imageio.ImageIO;
import org.jfree.chart.*;
import com.wm.app.b2b.server.ServiceThread;
import com.wm.app.b2b.server.ISRuntimeException;
import com.wm.lang.ns.NSName;
import org.jfree.chart.renderer.category.BarRenderer3D;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.image.BufferedImage;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.axis.*;
import org.jfree.chart.plot.*;
import org.jfree.ui.*;
import org.jfree.util.UnitType;
import org.jfree.data.xy.*;
import com.wm.app.b2b.server.ServerAPI;
import java.rmi.server.UID;
import java.awt.Color;
import org.jfree.chart.title.TextTitle;
// --- <<IS-END-IMPORTS>> ---

public final class chart

{
	// ---( internal utility methods )---

	final static chart _instance = new chart();

	static chart _newInstance() { return new chart(); }

	static chart _cast(Object o) { return (chart)o; }

	// ---( server methods )---




	public static final void cleanChart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(cleanChart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		try
		{
			String svc = "SimpleChart.util:remove";
			NSName nsName = NSName.create(svc);
			ServiceThread thrd = Service.doThreadInvoke(nsName,pipeline,-1);
			//thrd.getData();
		}
		catch (ISRuntimeException ise)
		{
			
		}
		catch (Exception e)
		{
			throw new ServiceException(e.getMessage());
		}
		// --- <<IS-END>> ---

                
	}



	public static final void create_chart (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(create_chart)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:optional chart_type {"BAR CHART","LINE CHART","AREA CHART"}
		// [i] field:0:optional efect {"2D","3D"}
		// [i] field:0:optional color_scheme {"NORMAL","GRADIANT"}
		// [i] field:0:optional sub_title
		// [i] field:0:optional h_size
		// [i] field:0:optional v_size
		// [i] field:0:optional orientation {"VERTICAL","HORIZONTAL"}
		// [i] field:0:required chart_title
		// [i] field:0:required cat_title
		// [i] field:0:required range_title
		// [i] field:1:required category
		// [i] record:1:required data_series_set
		// [i] - field:0:required series
		// [i] - field:1:required values
		// [o] field:0:required chart_file
		ArrayList al_series = new ArrayList();
		ArrayList al_data = new ArrayList();
		
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	chart_type = IDataUtil.getString( pipelineCursor, "chart_type" );
			String	chart_efect = IDataUtil.getString( pipelineCursor, "efect" );
			String	color_scheme = IDataUtil.getString( pipelineCursor, "color_scheme" );
			String	chart_title = IDataUtil.getString( pipelineCursor, "chart_title" );
			String	sub_title = IDataUtil.getString( pipelineCursor, "sub_title" );
			int h_size = IDataUtil.getInt( pipelineCursor, "h_size", 900);
			int v_size = IDataUtil.getInt( pipelineCursor, "v_size", 500);
			String	orientation = IDataUtil.getString( pipelineCursor, "orientation" );
			String	cat_title = IDataUtil.getString( pipelineCursor, "cat_title" );
			String	range_title = IDataUtil.getString( pipelineCursor, "range_title" );
			String[] category = IDataUtil.getStringArray( pipelineCursor, "category" );
		
			if (orientation == null)
				orientation = "VERTICAL";
			if (chart_type == null)
				chart_type = "BAR CHART";
			if (chart_efect == null)
				chart_efect = "2D";
			// data_category_set
			IData[]	data_series_set = IDataUtil.getIDataArray( pipelineCursor, "data_series_set" );
			if ( data_series_set != null)
			{
				for ( int i = 0; i < data_series_set.length; i++ )
				{
					IDataCursor data_series_setCursor = data_series_set[i].getCursor();
						String series = IDataUtil.getString( data_series_setCursor, "series" );
						//String	value = IDataUtil.getString( data_series_setCursor, "value" );
						String[] values = IDataUtil.getStringArray( data_series_setCursor, "values" );
					data_series_setCursor.destroy();
					al_series.add(series);
					al_data.add(values);			
				}
			}
		pipelineCursor.destroy();
		//////////////////////////////////////////////////////////////////////
		// Get file name
		File f = ServerAPI.getPackageConfigDir("SimpleChart");
		String file_path = f.getPath().substring(0,f.getPath().length()-6);
		String name = getChartFileName();
		String chart_file_io = file_path + "pub" + File.separator + "images" + File.separator + "charts" + File.separator + name;
		String chart_file_display = "images" + File.separator + "charts" + File.separator + name;
		///////////////////////////////////////////////////////////////////////
		boolean color_grade = false;
		if (color_scheme != null && color_scheme.equals("GRADIANT"))
		    color_grade = true;
		
		String[] series_list = new String[al_series.size()];
		double[][] data_list = new double[al_series.size()][category.length];
		for (int i = 0; i < al_series.size(); i++)
		{
		     series_list[i] = (String)al_series.get(i);	
		
		     String[] lst = (String[])al_data.get(i); 
		     for (int j = 0; j < lst.length; j++)
		     {
			String dbl = lst[j];	
		     	data_list[i][j] = Double.parseDouble(dbl);	
		     }
		}
		
		if (chart_type.equals("BAR CHART"))
		{	
		        try
		        {		
			    JFreeChart ch = null;
			    if (chart_efect.equals("3D"))
				ch = build3DBarChart(chart_title,sub_title,cat_title,range_title,series_list,category,data_list,color_grade,orientation);
			    else	
		        	ch = buildChart(chart_title,sub_title,cat_title,range_title,series_list,category,data_list,color_grade,orientation);
		
		            OutputStream out = new FileOutputStream(chart_file_io);
		            writeAsPNG( ch, out, h_size, v_size );
			    out.close();
		        }
		        catch(Exception e)
		        {
		        	//e.printStackTrace();	
				//err=e.getMessage();	
		        }
		}
		else if (chart_type.equals("AREA CHART"))
		{
		        try
		        {		
		        	JFreeChart ch = buildAreaChart(chart_title,sub_title,cat_title,range_title,series_list,category,data_list,orientation);
		        	OutputStream out = new FileOutputStream(chart_file_io);
		        	writeAsPNG( ch, out, h_size, v_size );
				out.close();
		        }
		        catch(Exception e)
		        {
		        	//e.printStackTrace();	
				//err=e.getMessage();	
		        }
		}
		else
		{
		        try
		        {		
		        	JFreeChart ch = buildLineChart(chart_title,sub_title,cat_title,range_title,series_list,category,data_list,orientation);
		        	OutputStream out = new FileOutputStream(chart_file_io);
		        	writeAsPNG( ch, out, h_size, v_size );
				out.close();
		        }
		        catch(Exception e)
		        {
		        	//e.printStackTrace();	
				//err=e.getMessage();	
		        }
		}
		
		// pipeline
		IDataCursor pipelineCursor_1 = pipeline.getCursor();
		IDataUtil.put( pipelineCursor_1, "chart_file", chart_file_display );
		//IDataUtil.put( pipelineCursor_1, "err", err );
		
		pipelineCursor_1.destroy();
		
		
		
		
		// --- <<IS-END>> ---

                
	}



	public static final void initEnv (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(initEnv)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// Set Java env config for UNix systems without display
		System.setProperty("java.awt.headless","true");
		
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
	private static String getTimeRangeString(String s)
	{
		if (s == null)
			return null;
		if (s.length() == 0)
			return null;
		int rng = Integer.parseInt(s);
		int upper_rng = 0;
		if (rng == 24)
			upper_rng = 1;
		else
			upper_rng = rng + 1;

		String str_range = Integer.toString(rng)+":00:00 - "+Integer.toString(upper_rng)+":00:00";
		
		return str_range;
	}

	private static String getDateString(String d)
	{
		String date = d;
		String pattern_in = "yyyy-MM-dd HH:mm:ss.SSS";
		SimpleDateFormat df = new SimpleDateFormat(pattern_in); 
		try
		{
			Date dt1 = df.parse(d);
			//date = Integer.toString(dt1.getYear())+"-"+Integer.toString(dt1.getMonth())+"-"+Integer.toString(dt1.getDay());
			df = new SimpleDateFormat("yyyy-MM-dd"); 
			date = df.format(dt1);
		}
		catch(Exception e)
		{
			date = d.substring(0,9);
			return date;
		}

		return date;
	}

	/**
	 * Generate unique file name to store chart.
	 */
	private static String getChartFileName()
	{
		String name = "";
		UID id = new UID();
		String t = id.toString().replace(':','_');
		name = t.replace('-','a');
		return name+".png";
	}
    private static boolean same_day(String d1, String d2)
	{
		boolean b = true;
		String pattern_in = "yyyy-MM-dd HH:mm:ss.SSS z";
		SimpleDateFormat df = new SimpleDateFormat(pattern_in); 
		try
		{
			Date dt1 = df.parse(d1);
			Date dt2 = df.parse(d2);
			if (dt1.getYear() == dt2.getYear() && 
				dt1.getMonth() == dt2.getMonth() &&
				dt1.getDay() == dt2.getDay())
				b = true;
			else
				b = false;
		}
		catch(Exception e)
		{
			return false;
		}

		return b;
	}

	public static JFreeChart build3DBarChart(String title,String sub_title, String cat_lbl,String rng_title,String[] series, String[] category_list, double[][] val, boolean color_grade,String orientation)
    {
        CategoryDataset dataset = createDataset(series,category_list,val);
        JFreeChart chart = create3DBarChart(title,sub_title,cat_lbl,rng_title,orientation,dataset,series,color_grade);
        return chart;
    }

	public static JFreeChart buildChart(String title,String sub_title, String cat_lbl,String rng_title,String[] series, String[] category_list, double[][] val, boolean color_grade,String orientation)
    {
        CategoryDataset dataset = createDataset(series,category_list,val);
        JFreeChart chart = createChart(title,sub_title,cat_lbl,rng_title,orientation,dataset,series,color_grade);
        return chart;
    }

	public static JFreeChart buildLineChart(String title,String sub_title,String cat_lbl,String rng_title,String[] series, String[] category_list, double[][] val,String orientation)
    {
        CategoryDataset dataset = createDataset(series,category_list,val);
        JFreeChart chart = createLineChart(title,sub_title,cat_lbl,rng_title,orientation,dataset,series);
        return chart;
    }

	public static JFreeChart buildAreaChart(String title,String sub_title,String cat_lbl,String rng_title,String[] series, String[] category_list, double[][] val,String orientation)
    {
        CategoryDataset dataset = createDataset(series,category_list,val);
        JFreeChart chart = createAreaChart(title,sub_title,cat_lbl,rng_title,orientation,dataset,series);
        return chart;
    }

    private static CategoryDataset createDataset(String[] series, String[] category_list, double[][] val) 
	{
        // create the dataset...
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        	for (int i = 0; i < series.length; i++)
        	{
        		for (int j = 0; j < category_list.length; j++)
        		{
       				dataset.addValue(val[i][j], series[i], category_list[j]);
        		}
			}
        
        return dataset;     
    }

    private static JFreeChart create3DBarChart(String title,
										  		String sub_title,
										  		String cat_lbl,
										  		String rng_title,
												String orientation, 
										  		CategoryDataset dataset,
										  		String[] series, boolean color_grade) 
	{
    	if (title == null)    
			title = "3D Bar Chart";
		JFreeChart jfreechart = null;
		if (orientation.equals("HORIZONTAL"))
		{
        	jfreechart = ChartFactory.createBarChart3D (
								title, 
								cat_lbl, 
								rng_title, 
								dataset, 
								PlotOrientation.HORIZONTAL, //PlotOrientation.VERTICAL, 
								true, true, false);
		}
		else
		{
        	jfreechart = ChartFactory.createBarChart3D (
								title, 
								cat_lbl, 
								rng_title, 
								dataset, 
								PlotOrientation.VERTICAL, 
								true, true, false);
		}
		if (sub_title!=null)
			jfreechart.addSubtitle(new TextTitle(sub_title));

        CategoryPlot categoryplot = jfreechart.getCategoryPlot();
        categoryplot.setDomainGridlinesVisible(true);
        CategoryAxis categoryaxis = categoryplot.getDomainAxis();
        categoryaxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(0.39269908169872414D));
        BarRenderer3D barrenderer3d = (BarRenderer3D)categoryplot.getRenderer();
        barrenderer3d.setDrawBarOutline(false);

        return jfreechart;
	}

    private static JFreeChart createChart(String title,
										  String sub_title,
										  String cat_lbl,
										  String rng_title,
										  String orientation, 
										  CategoryDataset dataset,
										  String[] series, boolean color_grade) 
	{
    	if (title == null)    
			title = "Bar Chart";
        // create the chart...
		JFreeChart chart = null;
		if (orientation.equals("HORIZONTAL"))
		{
        	chart = ChartFactory.createBarChart(
            		title,         			  // chart title
            		cat_lbl,                  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.HORIZONTAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}
		else
		{
        	chart = ChartFactory.createBarChart(
            		title,         			  // chart title
            		cat_lbl,                  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.VERTICAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}
		if (sub_title!=null)
			chart.addSubtitle(new TextTitle(sub_title));

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

		if (color_grade)       
		{
        	// set up gradient paints for series...
			for (int i = 0; i < series.length; i++)
			{
				GradientPaint gp = null;
       			gp = new GradientPaint(
            		0.0f, 0.0f, getColor(i), 
            		0.0f, 0.0f, new Color(0, 0, 64)
       			);
				renderer.setSeriesPaint(i, gp);
			}
		}

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );
        // OPTIONAL CUSTOMISATION COMPLETED.
        
        return chart;
        
    }

    private static JFreeChart createLineChart(String title,
											  String sub_title,
											  String cat_lbl,
											  String rng_title,
											  String orientation, 
											  CategoryDataset dataset,
											  String[] series) 
	{
    	if (title == null)    
			title = "Line Chart";
        // create the Line chart...
		JFreeChart chart = null;
		if (orientation.equals("HORIZONTAL"))
		{
        	chart = ChartFactory.createLineChart(
            		title,         			  // chart title
            		cat_lbl,               	  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.HORIZONTAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}
		else
		{
        	chart = ChartFactory.createLineChart(
            		title,         			  // chart title
            		cat_lbl,               	  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.VERTICAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}
		if (sub_title != null)
			chart.addSubtitle(new TextTitle(sub_title));

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        // get a reference to the plot for further customisation...
        CategoryPlot plot = chart.getCategoryPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		// Set line shape rendering
		LineAndShapeRenderer lineandshaperendere = (LineAndShapeRenderer)plot.getRenderer();
		lineandshaperendere.setShapesVisible(true);
		lineandshaperendere.setShapesFilled(true);
		lineandshaperendere.setDrawOutlines(true);
		//lineandshaperendere.setUseFillPaint(true);
		//lineandshaperendere.setFillPaint(Color.white);
	
        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
        );

 		return chart;
	}

    private static JFreeChart createAreaChart(String title,
											  String sub_title,
											  String cat_lbl,
											  String rng_title,
											  String orientation, 
											  CategoryDataset dataset,
											  String[] series) 
	{
    	if (title == null)    
			title = "Area Chart";
        // create the Area chart...
		JFreeChart chart = null;
		if (orientation.equals("HORIZONTAL"))
		{
        	chart = ChartFactory.createAreaChart(
            		title,         			  // chart title
            		cat_lbl,               	  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.HORIZONTAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}
		else
		{
        	chart = ChartFactory.createAreaChart(
            		title,         			  // chart title
            		cat_lbl,               	  // domain axis label
            		rng_title,                // range axis label
            		dataset,                  // data
            		PlotOrientation.VERTICAL, // orientation
            		true,                     // include legend
            		true,                     // tooltips?
            		false                     // URLs?
        	);
		}


		if (sub_title != null)
		{
        	//texttitle.setFont(new Font("SansSerif", 0, 12));
        	//texttitle.setPosition(RectangleEdge.TOP);
        	//texttitle.setPadding(new RectangleInsets(UnitType.RELATIVE, 0.050000000000000003D, 0.050000000000000003D, 0.050000000000000003D, 0.050000000000000003D));
        	//texttitle.setVerticalAlignment(VerticalAlignment.BOTTOM);

			chart.addSubtitle(new TextTitle(sub_title));
		}

        // set the background color for the chart...
        chart.setBackgroundPaint(Color.white);

        CategoryPlot categoryplot = chart.getCategoryPlot();
        //categoryplot.setForegroundAlpha(0.5F);
        categoryplot.setAxisOffset(new RectangleInsets(5D, 5D, 5D, 5D));
        categoryplot.setBackgroundPaint(Color.lightGray);
        categoryplot.setDomainGridlinesVisible(true);
        categoryplot.setDomainGridlinePaint(Color.white);
        categoryplot.setRangeGridlinesVisible(true);
        categoryplot.setRangeGridlinePaint(Color.white);

        // set the range axis to display integers only...
        final NumberAxis rangeAxis = (NumberAxis) categoryplot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	
        CategoryAxis domainAxis = categoryplot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0));

		return chart;
	}

	private static Color getColor(int idx)
	{
		Color[] cl = {Color.blue,Color.green,Color.red,Color.yellow,Color.darkGray,Color.magenta,Color.orange,Color.cyan,Color.pink,Color.black};
		if (idx > cl.length)
			return Color.black;
		Color res = cl[idx];
		return res;
	}

    private static void writeAsPNG( JFreeChart chart, OutputStream out, int width, int height )
    {
    	try
    	{
    		BufferedImage chartImage = chart.createBufferedImage( width, height, null);
    		ImageIO.write( chartImage, "png", out );
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    }    
	// --- <<IS-END-SHARED>> ---
}

