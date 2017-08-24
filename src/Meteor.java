
/**
 * Program Name: Meteor.java 
 * Purpose: Meteor class for the game of ELI
 * Coder: Shunyi Wang
 * Date: July	29, 2017 
 */


import java.awt.*;

public class Meteor
{
  private double x_position, y_position, radius, velocity;
  private int hardness, hit;
  private boolean is_twin;
  private Color color;
  private Polygon shape;
  private int dimension;

	/**
	 * 	Constructor, create a new Meteor located at the top of the screen
	 */
	public Meteor()
	{
		x_position = Math.random() * (650.0 - 50.0 + 1.0) + 50.0;
		y_position = 50.0;
		radius = Math.random() * (50.0 - 20.0 + 1.0) + 20.0;
		velocity = Math.random() * (25.0 - 1.0 + 1.0) + 1.0;
		
		hardness = (int) (Math.random() * (4 - 1 + 1) + 1);
		hit = hardness;
		
		//can be twin only if child won't go out of the screen
		if((x_position + 2 * radius) <= 700 && (x_position - 2 * radius >= 0) )
		{
			if((int) (Math.random() * 10 ) == 0)			
				is_twin = true;
			else
				is_twin = false;
		}
		color = new Color(0.0f, 0.0f, 1.0f);
		dimension = 12;
		shape = ELIAlgorithm.generateShape(x_position, y_position, radius, dimension);
				
	}	

	/**
	 * Constructor, create a child meteor
	 * @param mom mother meteor
	 * @param deltaX delta x position offset
	 */
	public Meteor(Meteor mom, double deltaX)
	{
		x_position = mom.x_position + deltaX;
		y_position = mom.y_position;
		radius = mom.radius / 2;
		velocity = (int) (Math.random() * (25 - 1 + 1) + 1);
		hardness = mom.hardness;
		hit = hardness;
		dimension = 12;

		is_twin = false;
		
		color = new Color(0.0f, 0.0f, 1.0f);
		shape = ELIAlgorithm.generateShape(x_position, y_position, radius, dimension);

	}
	
	/**
	 *  Is this Meteor a twin Meteor?
	 *  @return a boolean as twin or not
	 */
	public boolean isTwin()
	{
		return is_twin;
	}	// Update the position of the Meteor based on its velocity
	
	/**
	 * update meteor's position
	 */
	public void updatePos()
	{
		y_position += velocity;
		shape = ELIAlgorithm.generateShape(x_position, y_position, radius, dimension);

	}
	
  /**
   * Getter of x_position
   * @return meteor's x position
   */
	double getX()
	{
		return x_position;
	}
	
  /**
   * Getter of y_position
   * @return meteor's y position
   */
	double getY()
	{
		return y_position;
	}
		
  /**
   * Getter of radius
   * @return meteor's radius
   */
	double getRadius()
	{
		return radius;
	}


	/**
	 * The particle beam has scored a hit on this Meteor. Reduce the hardness value
	 * of the Meteor by one and return the current amount of
	 * hardness value remaining for this Meteor.  
	 * @return hardness after hit
	 */
	public int scoredHit()
	{
		--hit;
		return hit;
	}
	
	/**
	 * Given a mouse (x, y) coordinate, decide if that location was close enough to hit this Meteor.
	 *@param mouseX mouse pressed x position
	 *@param mouseY mouse pressed y position
	 *@return a boolean as hit whether or not 
	 */
	public boolean getTarget(double mouseX, double mouseY)
	{

		double distance = Math.sqrt((mouseX - x_position) * (mouseX - x_position)
																+ (mouseY - y_position) * (mouseY - y_position));
		if(distance <= radius)
			return true;
		else 
			return false;
	}

	/**
	 * Get meteor's shape
	 * @return meteor's shape
	 */
	public Polygon getShape()
	{
		return shape;
	}
	
	/**
	 * Get meteor's color
	 * @return meteor's color
	 */
	public Color getColor()
	{
		color = new Color(1.0f - (float)hit/hardness, 0.0f, (float)hit/hardness);
		
		return color;
	}	

}//end class Meteor

