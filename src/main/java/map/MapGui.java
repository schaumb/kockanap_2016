package map;

import bean.ItemType;
import communication.Item;
import communication.TickMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

/**
 * Created by qqcs on 25/11/16.
 */
public class MapGui extends DataMap {
    private static final MapGui INSTANCE = new MapGui();

    static MapGui get() {
        return INSTANCE;
    }


    private JFrame frame;
    private MapPanel mapPanel;

    private MapGui() {
    }


    @Override
    public void initialize() {
        super.initialize();
        if(mapPanel == null) {
            mapPanel = new MapPanel();
            JFrame frame = new JFrame("GUI");
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            frame.add(mapPanel);
            frame.pack();
            frame.setVisible(true);
        }
    }

    @Override
    public void tick(TickMessage message) {
        super.tick(message);

        mapPanel.invalidate();
        mapPanel.repaint();
    }

    private class MapPanel extends JPanel {

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 700);
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            graphics.fillRect(0, 0, 700, 700);
            if(getMessage() == null) return;

            for(Item item : getMessage().getItemList()) {
                Color color = item.getType().getColor();

                if(item.getOwnerId() == getOurId()) {
                    if(item.getType() == ItemType.TANK) {
                        color = Color.GREEN;

                    }
                    if(item.getType() == ItemType.ROCKET ||
                            item.getType() == ItemType.BULLET) {
                        color = color.darker().darker();
                    }
                }
                graphics.setColor(color);
                Graphics2D graphics2D = (Graphics2D) graphics;
                graphics2D.fill(item.getShape());
            }
        }

        private void printText(Graphics graphics, Point2D.Double position, int size,  Color color, String str) {
            graphics.setColor(color);
            graphics.drawString(str, (int) (position.getX() * 35) + size / 2, (int) (position.getY() * 35) + size / 2);

        }
    }
}
