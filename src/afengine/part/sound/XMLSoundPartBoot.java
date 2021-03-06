package afengine.part.sound;

import afengine.core.AbPartSupport;
import afengine.core.util.IXMLPartBoot;
import org.dom4j.Element;

/** 
 * <part name="SoundPart" path="afengine.part.sound.SoundPartBoot" />
 *
 * <SoundPart />
 * @author Albert Flex
 */
public class XMLSoundPartBoot implements IXMLPartBoot{
    
    @Override
    public AbPartSupport bootPart(Element element) {
        return new SoundPart();
    }    
}
