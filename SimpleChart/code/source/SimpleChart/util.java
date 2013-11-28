package SimpleChart;

// -----( IS Java Code Template v1.2
// -----( CREATED: 2006-07-28 17:06:26 JST
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
import org.jfree.data.category.CategoryDataset;
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
import com.wm.app.b2b.server.ServerAPI;
import java.rmi.server.UID;
// --- <<IS-END-IMPORTS>> ---

public final class util

{
	// ---( internal utility methods )---

	final static util _instance = new util();

	static util _newInstance() { return new util(); }

	static util _cast(Object o) { return (util)o; }

	// ---( server methods )---




	public static final void remove (IData pipeline)
        throws ServiceException
	{
		// --- <<IS-START(remove)>> ---
		// @subtype unknown
		// @sigtype java 3.5
		// [i] field:0:required chart_file
		try
		{
			Thread.sleep(10000);
		}
		catch(Exception e)
		{
		
		}
		// pipeline
		IDataCursor pipelineCursor = pipeline.getCursor();
			String	chart_file = IDataUtil.getString( pipelineCursor, "chart_file" );
		pipelineCursor.destroy();
		File f = ServerAPI.getPackageConfigDir("SimpleChart");
		String file_path = f.getPath().substring(0,f.getPath().length()-6);
		String chart_file_io = file_path + "pub" + File.separator + chart_file;
		f = new File(chart_file_io);
		if (f.exists())
			f.delete();
		// --- <<IS-END>> ---

                
	}

	// --- <<IS-START-SHARED>> ---
		private static String roundNumber(String n)
		{
			String res = n;
			if (n != null)
			{
				double dbl = Double.parseDouble(n);
				double d = Math.round(dbl * Math.pow(10,3.0))/Math.pow(10,3.0);
				res = Double.toString(d);
			}
			return res;
		}
		private static String getSenderID(IData data)
		{
			String res = null;
			if (data != null)
			{
		    	IDataCursor idc = data.getCursor();
		    	IData Request = IDataUtil.getIData(idc,"Request");
		    	if (Request != null)
		    	{
					//res+=" Request Found! ";
					IDataCursor idcRequest = Request.getCursor();
					IData Header = IDataUtil.getIData(idcRequest,"Header");
					if (Header != null)
					{
						//res+=" Header Found! ";
			    		IDataCursor idcHeader = Header.getCursor();
			    		res = IDataUtil.getString( idcHeader,"SenderOperatorID" );
			    		idcHeader.destroy();
					}
					idcRequest.destroy();
		    	}
		    	else
		    	idc.destroy();
			}
	
			return res;
		}
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
			String date = d.substring(0,9);
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
		private static String get_current_date()
		{
			String b = null;
			String pattern_in = "yyyy-MM-dd";
			SimpleDateFormat df = new SimpleDateFormat(pattern_in); 
			try
			{
				Date dt1 = new Date();
				b = df.format(dt1);
			}
			catch(Exception e)
			{
				return null;
			}
	
			return b;
	
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
		public static JFreeChart buildMNPChart(String title,String[] series, String[] category_list, double[] val)
	    {
	        CategoryDataset dataset = createMNPDataset(series,category_list,val);
	        JFreeChart chart = createChart(title,"Category","Values",dataset,series);
	        //ChartPanel chartPanel = new ChartPanel(chart, false);
	        //chartPanel.setPreferredSize(new Dimension(900, 500));
	        //setContentPane(chartPanel);
	        return chart;
	    }
	
		public static JFreeChart buildChart(String title,String cat_lbl,String rng_title,String[] series, String[] category_list, double[][] val)
	    {
	        CategoryDataset dataset = createDataset(series,category_list,val);
	        JFreeChart chart = createChart(title,cat_lbl,rng_title,dataset,series);
	        return chart;
	    }
	
	    private static CategoryDataset createMNPDataset(String[] series,String[] category_list, double[] val) {
	        
	        // row keys...
	        //String[] series_list = {"PortIn","PortOut","Routing"};
	        String[] series_list = series;
	
	        int size = 10;
	
	        // column keys...
	        //String[] category = new String[size];
	        //for (int i = 0; i < category.length; i++)
	        //	category[i]="Week "+Integer.toString(i);
	 
	        // create the dataset...
	        double start_value = 1.0;
	        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
			//for (int l = 0; l < val.length; l++)
			//{
	        	for (int k = 0; k < category_list.length; k++)
	        	{
	        		for (int j = 0; j < series_list.length; j++)
	        		{
	        			//double tmp = val[l]; //start_value + k + j;
	        			dataset.addValue(val[j], category_list[k], series_list[j]);
	        		}
				}
	        //}
	        
	        return dataset;
	        
	    }
	
	    private static CategoryDataset createDataset(String[] series, String[] category_list, double[][] val) 
		{
	
	        // create the dataset...
	        double start_value = 1.0;
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
	
	
	    private static JFreeChart createChart(String title,String cat_lbl,String rng_title, CategoryDataset dataset,String[] series) 
		{
	    	if (title == null)    
				title = "Chart Demo";
	        // create the chart...
	        JFreeChart chart = ChartFactory.createBarChart(
	            title,         // chart title
	            cat_lbl,               // domain axis label
	            rng_title,                  // range axis label
	            dataset,                  // data
	            PlotOrientation.VERTICAL, // orientation
	            true,                     // include legend
	            true,                     // tooltips?
	            false                     // URLs?
	        );
	
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
	        
	        // set up gradient paints for series...
			for (int i = 0; i < series.length; i++)
			{
				GradientPaint gp = null;
				if (i == 0)
				{
	        		gp = new GradientPaint(
	            	0.0f, 0.0f, Color.blue, 
	            	0.0f, 0.0f, new Color(0, 0, 64)
	        		);
				}
				else if (i == 1)
				{
	        		gp = new GradientPaint(
	            		0.0f, 0.0f, Color.green, 
	            		0.0f, 0.0f, new Color(0, 64, 0)
	        		);
				}
				else if (i == 2)
				{
	        		gp = new GradientPaint(
	            		0.0f, 0.0f, Color.red, 
	            		0.0f, 0.0f, new Color(64, 0, 0)
	        		);
				}
				else if (i == 3)
				{
	        		gp = new GradientPaint(
	            	0.0f, 0.0f, Color.yellow, 
	            	0.0f, 0.0f, new Color(0, 0, 64)
	        		);			
				}
				else if (i == 4)
				{
	        		gp = new GradientPaint(
	            	0.0f, 0.0f, Color.magenta, 
	            	0.0f, 0.0f, new Color(0, 0, 64)
	        		);			
				}
				else
				{
	        		gp = new GradientPaint(
	            	0.0f, 0.0f, Color.darkGray, 
	            	0.0f, 0.0f, new Color(0, 0, 64)
	        		);			
				}
				
				renderer.setSeriesPaint(i, gp);
			}
	/*****
	        GradientPaint gp0 = new GradientPaint(
	            0.0f, 0.0f, Color.darkGray, 
	            0.0f, 0.0f, new Color(0, 0, 64)
	        );
	        GradientPaint gp1 = new GradientPaint(
	            0.0f, 0.0f, Color.magenta, 
	            0.0f, 0.0f, new Color(0, 64, 0)
	        );
	        GradientPaint gp2 = new GradientPaint(
	            0.0f, 0.0f, Color.yellow, 
	            0.0f, 0.0f, new Color(64, 0, 0)
	        );
	        renderer.setSeriesPaint(0, gp0);
	        renderer.setSeriesPaint(1, gp1);
	        renderer.setSeriesPaint(2, gp2);
	****/
	        CategoryAxis domainAxis = plot.getDomainAxis();
	        domainAxis.setCategoryLabelPositions(
	            CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 6.0)
	        );
	        // OPTIONAL CUSTOMISATION COMPLETED.
	        
	        return chart;
	        
	    }
	 
	    public static void writeAsPNG( JFreeChart chart, OutputStream out, int width, int height )
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

