from django.contrib import admin
from .models import LegalAidRequest

@admin.register(LegalAidRequest)
class LegalAidRequestAdmin(admin.ModelAdmin):
    list_display = ['case_type', 'urgency_level', 'status', 'created_at']
    list_filter = ['case_type', 'urgency_level', 'status']
    search_fields = ['description', 'contact_info']
    readonly_fields = ['created_at']
    ordering = ['-created_at'] 