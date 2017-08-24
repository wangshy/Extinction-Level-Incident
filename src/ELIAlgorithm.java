
/**
 * Program Name: ELIAlgorithm.java 
 * Purpose: Algorith class for the game of ELI
 * Coder: Shunyi Wang
 * Date: July	29, 2017 
 */

import java.awt.Polygon;

public class ELIAlgorithm
{
	
	/** 
	 * Generate a polygon as meteor's shape
	 *@param x_position meteor's x position
	 *@param y_position meteor's y position
	 *@param radius meteor's radius
	 *@param dimension dimension of the polygon
	 *@return a polygon object as meteor's shape
	 */
	public static Polygon generateShape(double x_position, double y_position, double radius, int dimension)
	{

		double[] xpoints = new double[dimension];
		double[] ypoints = new double[dimension];

		//left vertex
		xpoints[0] = x_position - Math.random() * radius;
		ypoints[0] = y_position +	Math.sqrt( radius * radius - (xpoints[0] - x_position) * (xpoints[0] - x_position));

		//upper half vertexes
		for(int i = 1; i <= dimension/2 - 1; i++)
		{
			xpoints[i] = xpoints[i - 1] + Math.random() * (x_position + radius - xpoints[i - 1]) ;
			ypoints[i] = y_position +	Math.sqrt( radius * radius - (xpoints[i] - x_position) * (xpoints[i] - x_position));

		}
		
		//right vertex
		xpoints[dimension/2] = x_position + Math.random() * radius;
		ypoints[dimension/2] = y_position - Math.sqrt( radius * radius - (xpoints[dimension/2] - x_position) * (xpoints[dimension/2] - x_position));
		
		//lower half vertexes
		for(int i = dimension/2 + 1; i < dimension; i++)
		{
			xpoints[i] = xpoints[i - 1] - Math.random() * (xpoints[i - 1] - (x_position - radius));
			ypoints[i] = y_position - Math.sqrt( radius * radius - (xpoints[i] - x_position) * (xpoints[i] - x_position));

		}
		
		int[] xpoint = new int[dimension];
		int[] ypoint = new int[dimension];
		for(int i = 0; i < dimension; i++)
		{
			xpoint[i] = (int)xpoints[i];
			ypoint[i] = (int)ypoints[i];
		}


		return new Polygon(xpoint, ypoint, dimension);	

	}
	
	/**
	 * Generate a polygon as laser beam
	 *@param x1_position first point x position
	 *@param y1_position first point y position
	 *@param x2_position second point x position
	 *@param y2_position second point y position
	 *@param width beam width
	 *@return a polygon as laser beam's shape	 
	 */
	public static Polygon generateBeam(double x1_position, double y1_position, 
																			double x2_position, double y2_position, int width)
	{
		double length = Math.sqrt((x1_position - x2_position) * (x1_position - x2_position) + (y1_position - y2_position) * (y1_position - y2_position));
		double cos = Math.abs((x2_position - x1_position) / length);
		double sin = Math.abs((y2_position - y1_position) / length);
		
		int[] xpoints = new int[4];
		int[] ypoints = new int[4];
		
		xpoints[0] = (int)(x1_position + sin * width);
		ypoints[0] = (int)(y1_position + cos * width);
		
		xpoints[1] = (int)(x1_position - sin * width);
		ypoints[1] = (int)(y1_position - cos * width);
		
		xpoints[2] = (int)(x2_position - sin * width);
		ypoints[2] = (int)(y2_position - cos * width);
	
		xpoints[3] = (int)(x2_position + sin * width);
		ypoints[3] = (int)(y2_position + cos * width);

		return new Polygon(xpoints, ypoints, 4);
		
	}

}
 //end class