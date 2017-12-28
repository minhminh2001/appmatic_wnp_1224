package com.whitelabel.app.model;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.google.gson.annotations.SerializedName;
import com.whitelabel.app.Const;

import java.io.Serializable;
import java.util.List;

/**
 * Created by img on 2017/12/27.
 */

public class CategoryBaseBean extends SVRReturnEntity{
    /**
     * category : [{"id":0,"menu_id":"171","menu_type":"url","menu_title":"Dogs","brandId":"","brandName":"","level":1,"name":"","url":"","image":"","has_child":1,"children":[{"id":"271","menu_id":"785","menu_type":"category","menu_title":"Woof","brandId":"","brandName":"","level":2,"name":"Woof","url":"https://dev2.wnp.com.hk/for-dogs/woof.html","image":"","children":[{"id":"95","menu_id":"786","menu_type":"category","menu_title":"New Arrivals","brandId":"","brandName":"","level":3,"name":"New Arrivals","url":"https://dev2.wnp.com.hk/for-dogs/new-arrivals.html","image":""},{"id":"258","menu_id":"824","menu_type":"category","menu_title":"Its Hot Hot Hot!","brandId":"","brandName":"","level":3,"name":"Its Hot Hot Hot!","url":"https://dev2.wnp.com.hk/for-dogs/its-hot-hot-hot.html","image":""},{"id":"263","menu_id":"787","menu_type":"category","menu_title":"HK Pet Brands","brandId":"","brandName":"","level":3,"name":"HK Pet Brands","url":"https://dev2.wnp.com.hk/for-dogs/hk-pet-brands.html","image":""},{"id":"253","menu_id":"788","menu_type":"category","menu_title":"Organic","brandId":"","brandName":"","level":3,"name":"Organic","url":"https://dev2.wnp.com.hk/for-dogs/organic.html","image":""},{"id":"254","menu_id":"789","menu_type":"category","menu_title":"Eco-Friendly","brandId":"","brandName":"","level":3,"name":"Eco-Friendly","url":"https://dev2.wnp.com.hk/for-dogs/eco-friendly.html","image":""},{"id":"214","menu_id":"790","menu_type":"url","menu_title":"Sale","brandId":"","brandName":"","level":3,"name":"Sale","url":"https://dev2.wnp.com.hk/for-dogs/sale.html","image":""}]},{"id":"86","menu_id":"217","menu_type":"category","menu_title":"Food","brandId":"","brandName":"","level":2,"name":"Dog Food","url":"https://dev2.wnp.com.hk/for-dogs/food.html","image":"","children":[{"id":"107","menu_id":"791","menu_type":"category","menu_title":"Grain-Free Food","brandId":"","brandName":"","level":3,"name":"Grain-Free Food","url":"https://dev2.wnp.com.hk/for-dogs/food/grain-free-food.html","image":""},{"id":"202","menu_id":"792","menu_type":"category","menu_title":"Gluten-Free Food","brandId":"","brandName":"","level":3,"name":"Gluten-Free Food","url":"https://dev2.wnp.com.hk/for-dogs/food/gluten-free-food.html","image":""},{"id":"108","menu_id":"305","menu_type":"category","menu_title":"Dry Food","brandId":"","brandName":"","level":3,"name":"Dry Food","url":"https://dev2.wnp.com.hk/for-dogs/food/dry-food.html","image":""},{"id":"87","menu_id":"175","menu_type":"category","menu_title":"Canned Food","brandId":"","brandName":"","level":3,"name":"Canned Food","url":"https://dev2.wnp.com.hk/for-dogs/food/canned-food.html","image":""},{"id":"245","menu_id":"304","menu_type":"category","menu_title":"Freeze Dried / Dehydrated","brandId":"","brandName":"","level":3,"name":"Freeze Dried / Dehydrated","url":"https://dev2.wnp.com.hk/for-dogs/food/freeze-dried-food.html","image":""},{"id":"109","menu_id":"306","menu_type":"category","menu_title":"Frozen Food","brandId":"","brandName":"","level":3,"name":"Frozen Food","url":"https://dev2.wnp.com.hk/for-dogs/food/frozen-food.html","image":""}]},{"id":"133","menu_id":"277","menu_type":"category","menu_title":"Treats","brandId":"","brandName":"","level":2,"name":"Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats.html","image":"","children":[{"id":"134","menu_id":"325","menu_type":"category","menu_title":"Natural Chews & Rawhides","brandId":"","brandName":"","level":3,"name":"Natural Chews & Rawhides","url":"https://dev2.wnp.com.hk/for-dogs/treats/chews-rawhides.html","image":""},{"id":"136","menu_id":"327","menu_type":"category","menu_title":"Natural Meaty Treats","brandId":"","brandName":"","level":3,"name":"Natural Meaty Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/natural-meaty-treats.html","image":""},{"id":"228","menu_id":"754","menu_type":"category","menu_title":"Dental Treats","brandId":"","brandName":"","level":3,"name":"Dental Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/dental-treats.html","image":""},{"id":"137","menu_id":"328","menu_type":"category","menu_title":"Jerkies & Chewy","brandId":"","brandName":"","level":3,"name":"Jerkies & Chewy Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/jerky-chewy-treats.html","image":""},{"id":"135","menu_id":"326","menu_type":"category","menu_title":"Crunchy Cookies","brandId":"","brandName":"","level":3,"name":"Crunchy Cookies","url":"https://dev2.wnp.com.hk/for-dogs/treats/cookies.html","image":""}]},{"id":"140","menu_id":"280","menu_type":"category","menu_title":"Health Care","brandId":"","brandName":"","level":2,"name":"Health Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center.html","image":"","children":[{"id":"106","menu_id":"795","menu_type":"category","menu_title":"Flea & Tick ","brandId":"","brandName":"","level":3,"name":"Flea & Tick Control","url":"https://dev2.wnp.com.hk/for-dogs/flea-tick-control.html","image":""},{"id":"223","menu_id":"749","menu_type":"category","menu_title":"Hip & Joint Support","brandId":"","brandName":"","level":3,"name":"Hip & Joint Support","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/hip-joint-support.html","image":""},{"id":"104","menu_id":"269","menu_type":"category","menu_title":"Dental Care","brandId":"","brandName":"","level":3,"name":"Dental Care ","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/dental-care.html","image":""},{"id":"227","menu_id":"750","menu_type":"category","menu_title":"Skin & Coat","brandId":"","brandName":"","level":3,"name":"Skin & Coat Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/skin-coat.html","image":""},{"id":"113","menu_id":"308","menu_type":"category","menu_title":"Ear Care","brandId":"","brandName":"","level":3,"name":"Ear Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/ear-care.html","image":""},{"id":"224","menu_id":"751","menu_type":"category","menu_title":"Eye Care","brandId":"","brandName":"","level":3,"name":"Eye Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/eye-care.html","image":""},{"id":"225","menu_id":"752","menu_type":"category","menu_title":"Vitamins & Supplements","brandId":"","brandName":"","level":3,"name":"Vitamins & Supplements","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/vitamins-supplements.html","image":""},{"id":"230","menu_id":"757","menu_type":"category","menu_title":"Calming Aids","brandId":"","brandName":"","level":3,"name":"Calming Aids","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/calming-dogs.html","image":""},{"id":"226","menu_id":"753","menu_type":"category","menu_title":"Wormer & Remedies","brandId":"","brandName":"","level":3,"name":"Wormers & Remedies ","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/wormer.html","image":""},{"id":"234","menu_id":"760","menu_type":"category","menu_title":"Medical Supplies","brandId":"","brandName":"","level":3,"name":"Medical Supplies","url":"https://dev2.wnp.com.hk/for-dogs/medical-supplies.html","image":""}]},{"id":"111","menu_id":"273","menu_type":"category","menu_title":"Grooming","brandId":"","brandName":"","level":2,"name":"Grooming","url":"https://dev2.wnp.com.hk/for-dogs/grooming.html","image":"","children":[{"id":"112","menu_id":"307","menu_type":"category","menu_title":"Combs & Brushes","brandId":"","brandName":"","level":3,"name":"Brushes","url":"https://dev2.wnp.com.hk/for-dogs/grooming/brushes.html","image":""},{"id":"114","menu_id":"309","menu_type":"category","menu_title":"Nail Care","brandId":"","brandName":"","level":3,"name":"Nail Care","url":"https://dev2.wnp.com.hk/for-dogs/grooming/nail-care.html","image":""},{"id":"115","menu_id":"310","menu_type":"category","menu_title":"Shampoos & Conditioners","brandId":"","brandName":"","level":3,"name":"Shampoos & Conditioners","url":"https://dev2.wnp.com.hk/for-dogs/grooming/shampoos-conditioners.html","image":""},{"id":"116","menu_id":"311","menu_type":"category","menu_title":"Wipes & Sprays","brandId":"","brandName":"","level":3,"name":"Wipes & Sprays","url":"https://dev2.wnp.com.hk/for-dogs/grooming/wipes-sprays.html","image":""}]},{"id":"130","menu_id":"276","menu_type":"category","menu_title":"Toys","brandId":"","brandName":"","level":2,"name":"Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys.html","image":"","children":[{"id":"131","menu_id":"323","menu_type":"category","menu_title":"Fetch & Play Toys","brandId":"","brandName":"","level":3,"name":"Fetch & Play Dog Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/fetch-play-toys.html","image":""},{"id":"232","menu_id":"324","menu_type":"category","menu_title":"Cuddle Toys","brandId":"","brandName":"","level":3,"name":"Cuddle Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/cuddle-toys.html","image":""},{"id":"132","menu_id":"825","menu_type":"category","menu_title":"Learning Toys","brandId":"","brandName":"","level":3,"name":"Learning Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/learning-toys.html","image":""}]},{"id":"101","menu_id":"266","menu_type":"category","menu_title":"Clean Up","brandId":"","brandName":"","level":2,"name":"Clean Up","url":"https://dev2.wnp.com.hk/for-dogs/clean-up.html","image":"","children":[{"id":"229","menu_id":"756","menu_type":"category","menu_title":"Stain & Odor","brandId":"","brandName":"","level":3,"name":"Stain & Odor Remover","url":"https://dev2.wnp.com.hk/for-dogs/clean-up/stain-odor.html","image":""},{"id":"208","menu_id":"264","menu_type":"category","menu_title":"Wee Pads & Pick Up Bags","brandId":"","brandName":"","level":3,"name":"Wee Pads & Pick Up Bags","url":"https://dev2.wnp.com.hk/for-dogs/clean-up/wee-pads.html","image":""}]},{"id":"268","menu_id":"797","menu_type":"category","menu_title":"Supplies","brandId":"","brandName":"","level":2,"name":"Supplies-Dog","url":"https://dev2.wnp.com.hk/for-dogs/supplies-dog.html","image":"","children":[{"id":"98","menu_id":"798","menu_type":"category","menu_title":"Dog Beds","brandId":"","brandName":"","level":3,"name":"Beds","url":"https://dev2.wnp.com.hk/for-dogs/beds.html","image":""},{"id":"103","menu_id":"799","menu_type":"category","menu_title":"Crates & Houses","brandId":"","brandName":"","level":3,"name":"Crates & Houses","url":"https://dev2.wnp.com.hk/for-dogs/crates-houses.html","image":""},{"id":"110","menu_id":"800","menu_type":"category","menu_title":"Gates & Playpens","brandId":"","brandName":"","level":3,"name":"Gates & Playpens","url":"https://dev2.wnp.com.hk/for-dogs/gates.html","image":""},{"id":"105","menu_id":"801","menu_type":"category","menu_title":"Bowls & Feeders","brandId":"","brandName":"","level":3,"name":"Bowls & Feeders","url":"https://dev2.wnp.com.hk/for-dogs/feeders.html","image":""},{"id":"138","menu_id":"802","menu_type":"category","menu_title":"Water Fountains","brandId":"","brandName":"","level":3,"name":"Water Fountains","url":"https://dev2.wnp.com.hk/for-dogs/water-fountains.html","image":""},{"id":"102","menu_id":"803","menu_type":"category","menu_title":"Collars & Leashes","brandId":"","brandName":"","level":3,"name":"Collars & Leashes","url":"https://dev2.wnp.com.hk/for-dogs/collars-leashes.html","image":""},{"id":"259","menu_id":"804","menu_type":"category","menu_title":"Outdoor Adventure","brandId":"","brandName":"","level":3,"name":"Outdoor Adventure","url":"https://dev2.wnp.com.hk/for-dogs/outdoor-adventure.html","image":""},{"id":"100","menu_id":"805","menu_type":"category","menu_title":"Carriers","brandId":"","brandName":"","level":3,"name":"Carriers","url":"https://dev2.wnp.com.hk/for-dogs/carriers.html","image":""},{"id":"210","menu_id":"806","menu_type":"category","menu_title":"Travel","brandId":"","brandName":"","level":3,"name":"Travel","url":"https://dev2.wnp.com.hk/for-dogs/outing.html","image":""},{"id":"233","menu_id":"807","menu_type":"category","menu_title":"Training & Behavior","brandId":"","brandName":"","level":3,"name":"Dog Training & Behavior","url":"https://dev2.wnp.com.hk/for-dogs/training-behavior.html","image":""}]}]},{"id":0,"menu_id":"170","menu_type":"url","menu_title":"Cats","brandId":"","brandName":"","level":1,"name":"","url":"","image":"","has_child":1,"children":[{"id":"269","menu_id":"812","menu_type":"category","menu_title":"Meow","brandId":"","brandName":"","level":2,"name":"Meow","url":"https://dev2.wnp.com.hk/for-cats/meow.html","image":"","children":[{"id":"95","menu_id":"813","menu_type":"url","menu_title":"New Arrivals","brandId":"","brandName":"","level":3,"name":"New Arrivals","url":"https://dev2.wnp.com.hk/for-dogs/new-arrivals.html","image":""},{"id":"257","menu_id":"815","menu_type":"category","menu_title":"Organic","brandId":"","brandName":"","level":3,"name":"Feline Organic ","url":"https://dev2.wnp.com.hk/for-cats/organic.html","image":""},{"id":"214","menu_id":"814","menu_type":"url","menu_title":"Sale","brandId":"","brandName":"","level":3,"name":"Sale","url":"https://dev2.wnp.com.hk/for-dogs/sale.html","image":""}]},{"id":"150","menu_id":"290","menu_type":"category","menu_title":"Food","brandId":"","brandName":"","level":2,"name":"Cat Food","url":"https://dev2.wnp.com.hk/for-cats/food.html","image":"","children":[{"id":"276","menu_id":"821","menu_type":"category","menu_title":"Grain-Free Food","brandId":"","brandName":"","level":3,"name":"Grain-Free Food","url":"https://dev2.wnp.com.hk/for-cats/food/grain-free-food.html","image":""},{"id":"151","menu_id":"329","menu_type":"category","menu_title":"Canned Food","brandId":"","brandName":"","level":3,"name":"Canned Food","url":"https://dev2.wnp.com.hk/for-cats/food/canned-food.html","image":""},{"id":"153","menu_id":"331","menu_type":"category","menu_title":"Dry Food","brandId":"","brandName":"","level":3,"name":"Dry Food","url":"https://dev2.wnp.com.hk/for-cats/food/dry-food.html","image":""},{"id":"154","menu_id":"332","menu_type":"category","menu_title":"Food Pouch","brandId":"","brandName":"","level":3,"name":"Food Pouch","url":"https://dev2.wnp.com.hk/for-cats/food/food-pouch.html","image":""},{"id":"244","menu_id":"772","menu_type":"category","menu_title":"Freeze Dried Food","brandId":"","brandName":"","level":3,"name":"Freeze Dried Food","url":"https://dev2.wnp.com.hk/for-cats/food/freeze-dried-food.html","image":""},{"id":"155","menu_id":"333","menu_type":"category","menu_title":"Frozen Food","brandId":"","brandName":"","level":3,"name":"Frozen Food","url":"https://dev2.wnp.com.hk/for-cats/food/frozen-food.html","image":""},{"id":"152","menu_id":"330","menu_type":"category","menu_title":"Dehydrated Food","brandId":"","brandName":"","level":3,"name":"Dehydrated Food","url":"https://dev2.wnp.com.hk/for-cats/food/dehydrated-food.html","image":""}]},{"id":"158","menu_id":"172","menu_type":"category","menu_title":"Litter","brandId":"","brandName":"","level":2,"name":"Cat Litter","url":"https://dev2.wnp.com.hk/for-cats/cat-litter.html","image":"","children":[{"id":"158","menu_id":"293","menu_type":"category","menu_title":"Cat Litter","brandId":"","brandName":"","level":3,"name":"Cat Litter","url":"https://dev2.wnp.com.hk/for-cats/cat-litter.html","image":""},{"id":"159","menu_id":"286","menu_type":"category","menu_title":"Litter Boxes & Supplies","brandId":"","brandName":"","level":3,"name":"Litter Boxes & Supplies","url":"https://dev2.wnp.com.hk/for-cats/litter-supplies.html","image":""},{"id":"145","menu_id":"285","menu_type":"category","menu_title":"Stain & Odor Control","brandId":"","brandName":"","level":3,"name":"Stain & Odor Control for Cats","url":"https://dev2.wnp.com.hk/for-cats/clean-up.html","image":""}]},{"id":"165","menu_id":"298","menu_type":"category","menu_title":"Treats","brandId":"","brandName":"","level":2,"name":"Treats","url":"https://dev2.wnp.com.hk/for-cats/treats.html","image":"","children":[{"id":"272","menu_id":"817","menu_type":"category","menu_title":"Natural Meaty Treats","brandId":"","brandName":"","level":3,"name":"Natural Meaty Treats","url":"https://dev2.wnp.com.hk/for-cats/treats/natural-meaty-treats.html","image":""},{"id":"273","menu_id":"818","menu_type":"category","menu_title":"Dental Treats","brandId":"","brandName":"","level":3,"name":"Dental Treats","url":"https://dev2.wnp.com.hk/for-cats/treats/dental-treats.html","image":""},{"id":"144","menu_id":"284","menu_type":"category","menu_title":"Catnip & Cat Grass","brandId":"","brandName":"","level":3,"name":"Catnip & Cat Grass","url":"https://dev2.wnp.com.hk/for-cats/catnip-cat-grass.html","image":""}]},{"id":"168","menu_id":"301","menu_type":"category","menu_title":"Health Care","brandId":"","brandName":"","level":2,"name":"Health Care for Cats","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care.html","image":"","children":[{"id":"148","menu_id":"288","menu_type":"category","menu_title":"Dental Care","brandId":"","brandName":"","level":3,"name":"Dental Care","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-dental-care.html","image":""},{"id":"235","menu_id":"763","menu_type":"category","menu_title":"Ear & Eye Care","brandId":"","brandName":"","level":3,"name":"Ear & Eye Care","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-ear-eye.html","image":""},{"id":"237","menu_id":"765","menu_type":"category","menu_title":"Hip & Joint Support","brandId":"","brandName":"","level":3,"name":"Hip & Joint Support for Cats","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-hip-joint.html","image":""},{"id":"236","menu_id":"764","menu_type":"category","menu_title":"Hairball Prevention","brandId":"","brandName":"","level":3,"name":"Hairball Prevention","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-hairball.html","image":""},{"id":"238","menu_id":"766","menu_type":"category","menu_title":"Skin & Coat Care","brandId":"","brandName":"","level":3,"name":"Skin & Coat Care for Cats","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-skin-coat.html","image":""},{"id":"239","menu_id":"767","menu_type":"category","menu_title":"Vitamins & Supplements","brandId":"","brandName":"","level":3,"name":"Vitamins & Supplements","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-vitamins-supplements.html","image":""},{"id":"240","menu_id":"768","menu_type":"category","menu_title":"Calming Aid","brandId":"","brandName":"","level":3,"name":"Calming Aids","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-calming-aid.html","image":""},{"id":"243","menu_id":"771","menu_type":"category","menu_title":"Remedies for Cats","brandId":"","brandName":"","level":3,"name":"Remedies for Cats","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-remedies.html","image":""},{"id":"241","menu_id":"769","menu_type":"category","menu_title":"Flea & Tick Prevention","brandId":"","brandName":"","level":3,"name":"Flea & Tick Prevention","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-flea-tick.html","image":""},{"id":"242","menu_id":"770","menu_type":"category","menu_title":"Medical Supplies","brandId":"","brandName":"","level":3,"name":"Medical Supplies for Cats","url":"https://dev2.wnp.com.hk/for-cats/cats-health-care/cat-medical-supplies.html","image":""}]},{"id":"156","menu_id":"291","menu_type":"category","menu_title":"Grooming","brandId":"","brandName":"","level":2,"name":"Cat Grooming","url":"https://dev2.wnp.com.hk/for-cats/cat-grooming.html","image":"","children":[{"id":"274","menu_id":"819","menu_type":"category","menu_title":"Shampoos & Wipes","brandId":"","brandName":"","level":3,"name":"Shampoos & Wipes","url":"https://dev2.wnp.com.hk/for-cats/cat-grooming/shampoos-wipes.html","image":""},{"id":"275","menu_id":"820","menu_type":"category","menu_title":"Brushes & Nail Care","brandId":"","brandName":"","level":3,"name":"Brushes & Nail Care","url":"https://dev2.wnp.com.hk/for-cats/cat-grooming/brushes-nail-care.html","image":""}]},{"id":"162","menu_id":"297","menu_type":"category","menu_title":"Toys","brandId":"","brandName":"","level":2,"name":"Cat Toys","url":"https://dev2.wnp.com.hk/for-cats/toys.html","image":"","children":[{"id":"283","menu_id":"823","menu_type":"category","menu_title":"Interactive Cat Toys","brandId":"","brandName":"","level":3,"name":"Interactive Cat Toys","url":"https://dev2.wnp.com.hk/for-cats/toys/interactive-cat-toys.html","image":""},{"id":"164","menu_id":"335","menu_type":"category","menu_title":"Catnip Toys","brandId":"","brandName":"","level":3,"name":"CatnipToys","url":"https://dev2.wnp.com.hk/for-cats/toys/smart-toys.html","image":""}]},{"id":"270","menu_id":"218","menu_type":"category","menu_title":"Supplies","brandId":"","brandName":"","level":2,"name":"Supplies-Cat","url":"https://dev2.wnp.com.hk/for-cats/supplies-cat.html","image":"","children":[{"id":"146","menu_id":"809","menu_type":"category","menu_title":"Beds & Climbers","brandId":"","brandName":"","level":3,"name":"Beds & Climbers","url":"https://dev2.wnp.com.hk/for-cats/climber-house.html","image":""},{"id":"160","menu_id":"295","menu_type":"category","menu_title":"Scratchers","brandId":"","brandName":"","level":3,"name":"Scratchers","url":"https://dev2.wnp.com.hk/for-cats/scratchers.html","image":""},{"id":"143","menu_id":"810","menu_type":"category","menu_title":"Cat Behavior & Training","brandId":"","brandName":"","level":3,"name":"Cat Behavior & Training Aid","url":"https://dev2.wnp.com.hk/for-cats/cat-training.html","image":""},{"id":"149","menu_id":"289","menu_type":"category","menu_title":"Bowls & Feeders","brandId":"","brandName":"","level":3,"name":"Bowls & Feeders","url":"https://dev2.wnp.com.hk/for-cats/feeders.html","image":""},{"id":"166","menu_id":"299","menu_type":"category","menu_title":"Water Fountains","brandId":"","brandName":"","level":3,"name":"Water Fountains","url":"https://dev2.wnp.com.hk/for-cats/water-fountains.html","image":""},{"id":"147","menu_id":"287","menu_type":"category","menu_title":"Collars & Leashes","brandId":"","brandName":"","level":3,"name":"Collars & Leashes","url":"https://dev2.wnp.com.hk/for-cats/collars-leashes.html","image":""}]}]},{"id":0,"menu_id":"816","menu_type":"url","menu_title":"SHOP BY BRAND","brandId":"","brandName":"","level":1,"name":"","url":"","image":"","has_child":0}]
     * status : 1
     */

    private int status;
    private List<CategoryBean> category;

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<CategoryBean> getCategory() {
        return category;
    }

    public void setCategory(List<CategoryBean> category) {
        this.category = category;
    }

    public static class CategoryBean extends AbstractExpandableItem<CategoryBean.ChildrenBeanX> {
        /**
         * id : 0
         * menu_id : 171
         * menu_type : url
         * menu_title : Dogs
         * brandId :
         * brandName :
         * level : 1
         * name :
         * url :
         * image :
         * has_child : 1
         * children : [{"id":"271","menu_id":"785","menu_type":"category","menu_title":"Woof","brandId":"","brandName":"","level":2,"name":"Woof","url":"https://dev2.wnp.com.hk/for-dogs/woof.html","image":"","children":[{"id":"95","menu_id":"786","menu_type":"category","menu_title":"New Arrivals","brandId":"","brandName":"","level":3,"name":"New Arrivals","url":"https://dev2.wnp.com.hk/for-dogs/new-arrivals.html","image":""},{"id":"258","menu_id":"824","menu_type":"category","menu_title":"Its Hot Hot Hot!","brandId":"","brandName":"","level":3,"name":"Its Hot Hot Hot!","url":"https://dev2.wnp.com.hk/for-dogs/its-hot-hot-hot.html","image":""},{"id":"263","menu_id":"787","menu_type":"category","menu_title":"HK Pet Brands","brandId":"","brandName":"","level":3,"name":"HK Pet Brands","url":"https://dev2.wnp.com.hk/for-dogs/hk-pet-brands.html","image":""},{"id":"253","menu_id":"788","menu_type":"category","menu_title":"Organic","brandId":"","brandName":"","level":3,"name":"Organic","url":"https://dev2.wnp.com.hk/for-dogs/organic.html","image":""},{"id":"254","menu_id":"789","menu_type":"category","menu_title":"Eco-Friendly","brandId":"","brandName":"","level":3,"name":"Eco-Friendly","url":"https://dev2.wnp.com.hk/for-dogs/eco-friendly.html","image":""},{"id":"214","menu_id":"790","menu_type":"url","menu_title":"Sale","brandId":"","brandName":"","level":3,"name":"Sale","url":"https://dev2.wnp.com.hk/for-dogs/sale.html","image":""}]},{"id":"86","menu_id":"217","menu_type":"category","menu_title":"Food","brandId":"","brandName":"","level":2,"name":"Dog Food","url":"https://dev2.wnp.com.hk/for-dogs/food.html","image":"","children":[{"id":"107","menu_id":"791","menu_type":"category","menu_title":"Grain-Free Food","brandId":"","brandName":"","level":3,"name":"Grain-Free Food","url":"https://dev2.wnp.com.hk/for-dogs/food/grain-free-food.html","image":""},{"id":"202","menu_id":"792","menu_type":"category","menu_title":"Gluten-Free Food","brandId":"","brandName":"","level":3,"name":"Gluten-Free Food","url":"https://dev2.wnp.com.hk/for-dogs/food/gluten-free-food.html","image":""},{"id":"108","menu_id":"305","menu_type":"category","menu_title":"Dry Food","brandId":"","brandName":"","level":3,"name":"Dry Food","url":"https://dev2.wnp.com.hk/for-dogs/food/dry-food.html","image":""},{"id":"87","menu_id":"175","menu_type":"category","menu_title":"Canned Food","brandId":"","brandName":"","level":3,"name":"Canned Food","url":"https://dev2.wnp.com.hk/for-dogs/food/canned-food.html","image":""},{"id":"245","menu_id":"304","menu_type":"category","menu_title":"Freeze Dried / Dehydrated","brandId":"","brandName":"","level":3,"name":"Freeze Dried / Dehydrated","url":"https://dev2.wnp.com.hk/for-dogs/food/freeze-dried-food.html","image":""},{"id":"109","menu_id":"306","menu_type":"category","menu_title":"Frozen Food","brandId":"","brandName":"","level":3,"name":"Frozen Food","url":"https://dev2.wnp.com.hk/for-dogs/food/frozen-food.html","image":""}]},{"id":"133","menu_id":"277","menu_type":"category","menu_title":"Treats","brandId":"","brandName":"","level":2,"name":"Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats.html","image":"","children":[{"id":"134","menu_id":"325","menu_type":"category","menu_title":"Natural Chews & Rawhides","brandId":"","brandName":"","level":3,"name":"Natural Chews & Rawhides","url":"https://dev2.wnp.com.hk/for-dogs/treats/chews-rawhides.html","image":""},{"id":"136","menu_id":"327","menu_type":"category","menu_title":"Natural Meaty Treats","brandId":"","brandName":"","level":3,"name":"Natural Meaty Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/natural-meaty-treats.html","image":""},{"id":"228","menu_id":"754","menu_type":"category","menu_title":"Dental Treats","brandId":"","brandName":"","level":3,"name":"Dental Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/dental-treats.html","image":""},{"id":"137","menu_id":"328","menu_type":"category","menu_title":"Jerkies & Chewy","brandId":"","brandName":"","level":3,"name":"Jerkies & Chewy Treats","url":"https://dev2.wnp.com.hk/for-dogs/treats/jerky-chewy-treats.html","image":""},{"id":"135","menu_id":"326","menu_type":"category","menu_title":"Crunchy Cookies","brandId":"","brandName":"","level":3,"name":"Crunchy Cookies","url":"https://dev2.wnp.com.hk/for-dogs/treats/cookies.html","image":""}]},{"id":"140","menu_id":"280","menu_type":"category","menu_title":"Health Care","brandId":"","brandName":"","level":2,"name":"Health Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center.html","image":"","children":[{"id":"106","menu_id":"795","menu_type":"category","menu_title":"Flea & Tick ","brandId":"","brandName":"","level":3,"name":"Flea & Tick Control","url":"https://dev2.wnp.com.hk/for-dogs/flea-tick-control.html","image":""},{"id":"223","menu_id":"749","menu_type":"category","menu_title":"Hip & Joint Support","brandId":"","brandName":"","level":3,"name":"Hip & Joint Support","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/hip-joint-support.html","image":""},{"id":"104","menu_id":"269","menu_type":"category","menu_title":"Dental Care","brandId":"","brandName":"","level":3,"name":"Dental Care ","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/dental-care.html","image":""},{"id":"227","menu_id":"750","menu_type":"category","menu_title":"Skin & Coat","brandId":"","brandName":"","level":3,"name":"Skin & Coat Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/skin-coat.html","image":""},{"id":"113","menu_id":"308","menu_type":"category","menu_title":"Ear Care","brandId":"","brandName":"","level":3,"name":"Ear Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/ear-care.html","image":""},{"id":"224","menu_id":"751","menu_type":"category","menu_title":"Eye Care","brandId":"","brandName":"","level":3,"name":"Eye Care","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/eye-care.html","image":""},{"id":"225","menu_id":"752","menu_type":"category","menu_title":"Vitamins & Supplements","brandId":"","brandName":"","level":3,"name":"Vitamins & Supplements","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/vitamins-supplements.html","image":""},{"id":"230","menu_id":"757","menu_type":"category","menu_title":"Calming Aids","brandId":"","brandName":"","level":3,"name":"Calming Aids","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/calming-dogs.html","image":""},{"id":"226","menu_id":"753","menu_type":"category","menu_title":"Wormer & Remedies","brandId":"","brandName":"","level":3,"name":"Wormers & Remedies ","url":"https://dev2.wnp.com.hk/for-dogs/wellness-center/wormer.html","image":""},{"id":"234","menu_id":"760","menu_type":"category","menu_title":"Medical Supplies","brandId":"","brandName":"","level":3,"name":"Medical Supplies","url":"https://dev2.wnp.com.hk/for-dogs/medical-supplies.html","image":""}]},{"id":"111","menu_id":"273","menu_type":"category","menu_title":"Grooming","brandId":"","brandName":"","level":2,"name":"Grooming","url":"https://dev2.wnp.com.hk/for-dogs/grooming.html","image":"","children":[{"id":"112","menu_id":"307","menu_type":"category","menu_title":"Combs & Brushes","brandId":"","brandName":"","level":3,"name":"Brushes","url":"https://dev2.wnp.com.hk/for-dogs/grooming/brushes.html","image":""},{"id":"114","menu_id":"309","menu_type":"category","menu_title":"Nail Care","brandId":"","brandName":"","level":3,"name":"Nail Care","url":"https://dev2.wnp.com.hk/for-dogs/grooming/nail-care.html","image":""},{"id":"115","menu_id":"310","menu_type":"category","menu_title":"Shampoos & Conditioners","brandId":"","brandName":"","level":3,"name":"Shampoos & Conditioners","url":"https://dev2.wnp.com.hk/for-dogs/grooming/shampoos-conditioners.html","image":""},{"id":"116","menu_id":"311","menu_type":"category","menu_title":"Wipes & Sprays","brandId":"","brandName":"","level":3,"name":"Wipes & Sprays","url":"https://dev2.wnp.com.hk/for-dogs/grooming/wipes-sprays.html","image":""}]},{"id":"130","menu_id":"276","menu_type":"category","menu_title":"Toys","brandId":"","brandName":"","level":2,"name":"Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys.html","image":"","children":[{"id":"131","menu_id":"323","menu_type":"category","menu_title":"Fetch & Play Toys","brandId":"","brandName":"","level":3,"name":"Fetch & Play Dog Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/fetch-play-toys.html","image":""},{"id":"232","menu_id":"324","menu_type":"category","menu_title":"Cuddle Toys","brandId":"","brandName":"","level":3,"name":"Cuddle Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/cuddle-toys.html","image":""},{"id":"132","menu_id":"825","menu_type":"category","menu_title":"Learning Toys","brandId":"","brandName":"","level":3,"name":"Learning Toys","url":"https://dev2.wnp.com.hk/for-dogs/toys/learning-toys.html","image":""}]},{"id":"101","menu_id":"266","menu_type":"category","menu_title":"Clean Up","brandId":"","brandName":"","level":2,"name":"Clean Up","url":"https://dev2.wnp.com.hk/for-dogs/clean-up.html","image":"","children":[{"id":"229","menu_id":"756","menu_type":"category","menu_title":"Stain & Odor","brandId":"","brandName":"","level":3,"name":"Stain & Odor Remover","url":"https://dev2.wnp.com.hk/for-dogs/clean-up/stain-odor.html","image":""},{"id":"208","menu_id":"264","menu_type":"category","menu_title":"Wee Pads & Pick Up Bags","brandId":"","brandName":"","level":3,"name":"Wee Pads & Pick Up Bags","url":"https://dev2.wnp.com.hk/for-dogs/clean-up/wee-pads.html","image":""}]},{"id":"268","menu_id":"797","menu_type":"category","menu_title":"Supplies","brandId":"","brandName":"","level":2,"name":"Supplies-Dog","url":"https://dev2.wnp.com.hk/for-dogs/supplies-dog.html","image":"","children":[{"id":"98","menu_id":"798","menu_type":"category","menu_title":"Dog Beds","brandId":"","brandName":"","level":3,"name":"Beds","url":"https://dev2.wnp.com.hk/for-dogs/beds.html","image":""},{"id":"103","menu_id":"799","menu_type":"category","menu_title":"Crates & Houses","brandId":"","brandName":"","level":3,"name":"Crates & Houses","url":"https://dev2.wnp.com.hk/for-dogs/crates-houses.html","image":""},{"id":"110","menu_id":"800","menu_type":"category","menu_title":"Gates & Playpens","brandId":"","brandName":"","level":3,"name":"Gates & Playpens","url":"https://dev2.wnp.com.hk/for-dogs/gates.html","image":""},{"id":"105","menu_id":"801","menu_type":"category","menu_title":"Bowls & Feeders","brandId":"","brandName":"","level":3,"name":"Bowls & Feeders","url":"https://dev2.wnp.com.hk/for-dogs/feeders.html","image":""},{"id":"138","menu_id":"802","menu_type":"category","menu_title":"Water Fountains","brandId":"","brandName":"","level":3,"name":"Water Fountains","url":"https://dev2.wnp.com.hk/for-dogs/water-fountains.html","image":""},{"id":"102","menu_id":"803","menu_type":"category","menu_title":"Collars & Leashes","brandId":"","brandName":"","level":3,"name":"Collars & Leashes","url":"https://dev2.wnp.com.hk/for-dogs/collars-leashes.html","image":""},{"id":"259","menu_id":"804","menu_type":"category","menu_title":"Outdoor Adventure","brandId":"","brandName":"","level":3,"name":"Outdoor Adventure","url":"https://dev2.wnp.com.hk/for-dogs/outdoor-adventure.html","image":""},{"id":"100","menu_id":"805","menu_type":"category","menu_title":"Carriers","brandId":"","brandName":"","level":3,"name":"Carriers","url":"https://dev2.wnp.com.hk/for-dogs/carriers.html","image":""},{"id":"210","menu_id":"806","menu_type":"category","menu_title":"Travel","brandId":"","brandName":"","level":3,"name":"Travel","url":"https://dev2.wnp.com.hk/for-dogs/outing.html","image":""},{"id":"233","menu_id":"807","menu_type":"category","menu_title":"Training & Behavior","brandId":"","brandName":"","level":3,"name":"Dog Training & Behavior","url":"https://dev2.wnp.com.hk/for-dogs/training-behavior.html","image":""}]}]
         */

        private int id;
        @SerializedName("menu_id")
        private String menuId;
        @SerializedName("menu_type")
        private String menuType;
        @SerializedName("menu_title")
        private String menuTitle;
        private String brandId;
        private String brandName;
        private int level;
        private String name;
        private String url;
        private String image;
        private int has_child;
        private List<ChildrenBeanX> children;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getMenuId() {
            return menuId;
        }

        public void setMenuId(String menuId) {
            this.menuId = menuId;
        }

        public String getMenuType() {
            return menuType;
        }

        public void setMenuType(String menuType) {
            this.menuType = menuType;
        }

        public String getMenuTitle() {
            return menuTitle;
        }

        public void setMenuTitle(String menuTitle) {
            this.menuTitle = menuTitle;
        }

        public String getBrandId() {
            return brandId;
        }

        public void setBrandId(String brandId) {
            this.brandId = brandId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        @Override
        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getHas_child() {
            return has_child;
        }

        public void setHas_child(int has_child) {
            this.has_child = has_child;
        }

        public List<ChildrenBeanX> getChildren() {
            return children;
        }

        public void setChildren(List<ChildrenBeanX> children) {
            this.children = children;
        }

        public static class ChildrenBeanX extends AbstractExpandableItem<CategoryBean.ChildrenBeanX.ChildrenBean> implements MultiItemEntity,Serializable {
            /**
             * id : 271
             * menu_id : 785
             * menu_type : category
             * menu_title : Woof
             * brandId :
             * brandName :
             * level : 2
             * name : Woof
             * url : https://dev2.wnp.com.hk/for-dogs/woof.html
             * image :
             * children : [{"id":"95","menu_id":"786","menu_type":"category","menu_title":"New Arrivals","brandId":"","brandName":"","level":3,"name":"New Arrivals","url":"https://dev2.wnp.com.hk/for-dogs/new-arrivals.html","image":""},{"id":"258","menu_id":"824","menu_type":"category","menu_title":"Its Hot Hot Hot!","brandId":"","brandName":"","level":3,"name":"Its Hot Hot Hot!","url":"https://dev2.wnp.com.hk/for-dogs/its-hot-hot-hot.html","image":""},{"id":"263","menu_id":"787","menu_type":"category","menu_title":"HK Pet Brands","brandId":"","brandName":"","level":3,"name":"HK Pet Brands","url":"https://dev2.wnp.com.hk/for-dogs/hk-pet-brands.html","image":""},{"id":"253","menu_id":"788","menu_type":"category","menu_title":"Organic","brandId":"","brandName":"","level":3,"name":"Organic","url":"https://dev2.wnp.com.hk/for-dogs/organic.html","image":""},{"id":"254","menu_id":"789","menu_type":"category","menu_title":"Eco-Friendly","brandId":"","brandName":"","level":3,"name":"Eco-Friendly","url":"https://dev2.wnp.com.hk/for-dogs/eco-friendly.html","image":""},{"id":"214","menu_id":"790","menu_type":"url","menu_title":"Sale","brandId":"","brandName":"","level":3,"name":"Sale","url":"https://dev2.wnp.com.hk/for-dogs/sale.html","image":""}]
             */

            private String id;
            @SerializedName("menu_id")
            private String menuId;
            @SerializedName("menu_type")
            private String menuType;
            @SerializedName("menu_title")
            private String menuTitle;
            private String brandId;
            private String brandName;
            private int level;
            private String name;
            private String url;
            private String image;
            private List<ChildrenBean> children;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getMenuId() {
                return menuId;
            }

            public void setMenuId(String menuId) {
                this.menuId = menuId;
            }

            public String getMenuType() {
                return menuType;
            }

            public void setMenuType(String menuType) {
                this.menuType = menuType;
            }

            public String getMenuTitle() {
                return menuTitle;
            }

            public void setMenuTitle(String menuTitle) {
                this.menuTitle = menuTitle;
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getBrandName() {
                return brandName;
            }

            public void setBrandName(String brandName) {
                this.brandName = brandName;
            }

            @Override
            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public List<ChildrenBean> getChildren() {
                return children;
            }

            public void setChildren(List<ChildrenBean> children) {
                this.children = children;
            }

            @Override
            public int getItemType() {
                return Const.TYPE_TREE_LEVEL_0;
            }

            public static class ChildrenBean implements MultiItemEntity,Serializable{
                /**
                 * id : 95
                 * menu_id : 786
                 * menu_type : category
                 * menu_title : New Arrivals
                 * brandId :
                 * brandName :
                 * level : 3
                 * name : New Arrivals
                 * url : https://dev2.wnp.com.hk/for-dogs/new-arrivals.html
                 * image :
                 */

                private String id;
                @SerializedName("menu_id")
                private String menuId;
                @SerializedName("menu_type")
                private String menuType;
                @SerializedName("menu_title")
                private String menuTitle;
                private String brandId;
                private String brandName;
                private int level;
                private String name;
                private String url;
                private String image;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getMenuId() {
                    return menuId;
                }

                public void setMenuId(String menuId) {
                    this.menuId = menuId;
                }

                public String getMenuType() {
                    return menuType;
                }

                public void setMenuType(String menuType) {
                    this.menuType = menuType;
                }

                public String getMenuTitle() {
                    return menuTitle;
                }

                public void setMenuTitle(String menuTitle) {
                    this.menuTitle = menuTitle;
                }

                public String getBrandId() {
                    return brandId;
                }

                public void setBrandId(String brandId) {
                    this.brandId = brandId;
                }

                public String getBrandName() {
                    return brandName;
                }

                public void setBrandName(String brandName) {
                    this.brandName = brandName;
                }

                public int getLevel() {
                    return level;
                }

                public void setLevel(int level) {
                    this.level = level;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getImage() {
                    return image;
                }

                public void setImage(String image) {
                    this.image = image;
                }

                @Override
                public int getItemType() {
                    return Const.TYPE_TREE_LEVEL_1;
                }
            }
        }
    }
}

